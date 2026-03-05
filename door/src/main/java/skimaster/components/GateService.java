package skimaster.components;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GateService {
    private final Gate gate;
    public GateService(@Value("${gate.id}") String gateId,
                       @Value("${gate.alert-threshold}") int alertThreshold) {
        this.gate = new Gate(gateId, GateStatus.OPEN, alertThreshold);
    }
    public Optional<PassageResponseDTO> processPassage(@Valid PassageRequestDTO requestDTO){
        boolean accepted = gate.getStatus() == GateStatus.OPEN && requestDTO.accepted();
        if(!accepted){
            return Optional.empty();
        }
        gate.registerPassage(new Passage(requestDTO.nfcCardId(),accepted, LocalDate.now()));
        return Optional.of(new PassageResponseDTO(accepted,gate.getStatus()));
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
        return gate.getPassages().stream().filter(x-> Objects.equals(x.getNfcCardId(), nfcCardId)).map(passage -> new PassageDTO(passage.getNfcCardId(),passage.getTimestamp())).toList();
    }

    public IncidentDTO addIncident(@Valid IncidentRequestDTO incidentRequestDTO){
        closeGate();
        Incident incident = new Incident(incidentRequestDTO.incidentType(), incidentRequestDTO.description(), Instant.now());
        gate.registerIncident(incident);
        return new IncidentDTO(incident.getType(), incidentRequestDTO.description(),incident.getTimestamp());
    }

    public List<PassageDTO> getAllPassages() {
        return gate.getPassages().stream().map(x->new PassageDTO(x.getNfcCardId(), LocalDate.now())).toList();
    }

    public List<PassageDTO> getAllPassagesOfToday(){
        LocalDate today = LocalDate.now();
        return gate.getPassages().stream()
                .filter(p -> p.getTimestamp()
                        .equals(today))
                .map(p -> new PassageDTO(p.getNfcCardId(), p.getTimestamp()))
                .toList();
    }
}
