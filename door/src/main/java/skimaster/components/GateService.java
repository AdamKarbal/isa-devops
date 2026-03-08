package skimaster.components;

import fr.univcotedazur.skimaster.gate.components.Portier;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import skimaster.dtos.*;
import skimaster.entities.Gate;
import skimaster.entities.GateStatus;
import skimaster.entities.Incident;
import skimaster.entities.Passage;
import skimaster.exceptions.AlreadyClosedGateException;
import skimaster.exceptions.AlreadyOpenGateException;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class GateService {
    private final Gate gate;
    private Portier portier;
    public GateService(@Value("${gate.id}") String gateId,
                       @Value("${gate.alert-threshold}") int alertThreshold, Portier portier) {
        this.gate = new Gate(gateId, GateStatus.OPEN, alertThreshold);
        this.portier = portier;
    }

    public PassageResponseDTO processPassage(@Valid PassageRequestDTO requestDTO){
        boolean accepted = gate.getStatus() == GateStatus.OPEN && requestDTO.accepted();
        gate.registerPassage(new Passage(requestDTO.nfcCardId(),accepted, Instant.now()));
        return new PassageResponseDTO(accepted,gate.getStatus());
    }

    public void openGate(){
        if(gate.getStatus() == GateStatus.OPEN) throw new AlreadyOpenGateException("Portique déjà ouvert.");
        gate.setStatus(GateStatus.OPEN);

    }

    public void closeGate(){
        if(gate.getStatus() == GateStatus.CLOSED) throw new AlreadyClosedGateException("Portique déjà fermé.");
        gate.setStatus(GateStatus.CLOSED);
    }

    public GateStatusResponseDTO getStatus(){
        return new GateStatusResponseDTO(gate.getStatus(), gate.getAlertThreshold(), gate.getCurrentPassages(), gate.getIncidents());
    }

    public List<PassageDTO> getPassagesById(Long nfcCardId){
        return gate.getPassages().stream().filter(x-> Objects.equals(x.getNfcCardId(), nfcCardId)).map(passage -> new PassageDTO(passage.getNfcCardId(), passage.isAccepted(),passage.getTimestamp())).toList();
    }

    public IncidentDTO addIncident(@Valid IncidentRequestDTO incidentRequestDTO){
        closeGate();
        Incident incident = new Incident(incidentRequestDTO.incidentType(), incidentRequestDTO.description(), Instant.now());
        gate.registerIncident(incident);
        return new IncidentDTO(incident.getType(), incidentRequestDTO.description(),incident.getTimestamp());
    }

    public List<Long> getAllPassages() {
        List<Long> allIds = gate.getPassages().stream()
                .filter(skimaster.entities.Passage::isAccepted)
                .map(skimaster.entities.Passage::getNfcCardId)
                .toList();
        gate.getPassages().clear();

        return allIds;
    }
}
