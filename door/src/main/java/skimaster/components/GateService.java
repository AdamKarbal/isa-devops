package skimaster.components;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import skimaster.dtos.*;
import skimaster.entities.Gate;
import skimaster.entities.GateStatus;
import skimaster.entities.Incident;
import skimaster.entities.Passage;
import java.util.List;
import java.util.ArrayList;
import skimaster.exceptions.AlreadyClosedGateException;
import skimaster.exceptions.AlreadyOpenGateException;

import java.time.*;
import java.util.ArrayList;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GateService {
    private final Gate gate;
    private final List<LocalTime> openingTimes = new ArrayList<>();
    private final List<LocalTime> closingTimes = new ArrayList<>();
    private static final Logger LOG = LoggerFactory.getLogger(GateService.class);
    private List<Long> whitelist = new ArrayList<>();
    public GateService(@Value("${gate.id}") String gateId,
                       @Value("${gate.alert-threshold}") int alertThreshold) {
        this.gate = new Gate(gateId, GateStatus.OPEN, alertThreshold, LocalTime.of(7,0),LocalTime.of(17,30));
        openingTimes.add(this.gate.getStartTime());
        closingTimes.add(this.gate.getEndTime());
    }
    public void updateWhitelist(List<Long> authorizedIds) {

        this.whitelist = authorizedIds;
        LOG.info("GATE DEBUG: Whitelist mise à jour. Nouveaux IDs autorisés: {}", this.whitelist);
    }
    public Optional<PassageResponseDTO> processPassage(@Valid PassageRequestDTO requestDTO) {
        Long cardId = requestDTO.nfcCardId(); // Correction : on définit cardId ici
        boolean isAuthorized = whitelist.contains(cardId);

        // LOGS CRITIQUES POUR LE DEBUG
        LOG.info("GATE DEBUG: Tentative de passage pour la carte ID: {}", cardId);
        LOG.info("GATE DEBUG: Présent dans whitelist ? {}", isAuthorized);
        LOG.info("GATE DEBUG: Liste complète en mémoire: {}", whitelist);
        LOG.info("GATE DEBUG: Statut de la porte: {}", gate.getStatus());

        boolean accepted = (gate.getStatus() == GateStatus.OPEN) && isAuthorized;

        if (!accepted) {
            LOG.warn("GATE DEBUG: Passage REFUSÉ pour {}", cardId);
            return Optional.empty();
        }

        LOG.info("GATE DEBUG: Passage ACCEPTÉ pour {}", cardId);
        gate.registerPassage(new Passage(cardId, accepted, LocalDateTime.now()));
        return Optional.of(new PassageResponseDTO(accepted, gate.getStatus()));
    }

    public void openGate(){
        if(gate.getStatus() == GateStatus.OPEN) throw new AlreadyOpenGateException("Portique déjà ouvert.");
        gate.setStatus(GateStatus.OPEN);
        openingTimes.add(LocalTime.now());
    }

    public void closeGate(){
        if(gate.getStatus() == GateStatus.CLOSED) throw new AlreadyClosedGateException("Portique déjà fermé.");
        gate.setStatus(GateStatus.CLOSED);
        closingTimes.add(LocalTime.now());
    }

    public GateStatusResponseDTO getStatus(){
        return new GateStatusResponseDTO(gate.getStatus(), gate.getAlertThreshold(), gate.getCurrentPassages(), gate.getIncidents());
    }

    public List<PassageDTO> getPassagesById(Long nfcCardId){
        return gate.getPassages().stream().filter(x-> Objects.equals(x.getNfcCardId(), nfcCardId)).map(passage -> new PassageDTO(passage.getNfcCardId(),passage.getTimestamp(),gate.getGateId())).toList();
    }

    public IncidentDTO addIncident(@Valid IncidentRequestDTO incidentRequestDTO){
        closeGate();
        Incident incident = new Incident(incidentRequestDTO.incidentType(), incidentRequestDTO.description(), Instant.now());
        gate.registerIncident(incident);
        return new IncidentDTO(incident.getType(), incidentRequestDTO.description(),incident.getTimestamp());
    }

    public List<PassageDTO> getAllPassages() {
        return gate.getPassages().stream().map(x->new PassageDTO(x.getNfcCardId(), LocalDateTime.now(),gate.getGateId())).toList();
    }

    public void clearTimesList() {
        openingTimes.clear();
        closingTimes.clear();
    }

    public long getLastOpeningDuration(){

        List<Long> sumDuration = new ArrayList<>();
        int size = Math.min(openingTimes.size(), closingTimes.size());

        if (openingTimes.size()>closingTimes.size()){
            LocalTime lastOpening = openingTimes.getLast();
            sumDuration.add(Duration.between(lastOpening,Instant.now()).getSeconds());
        }
        for (int i=0;i<size;i++){

            long duration = Duration.between(openingTimes.get(i),closingTimes.get(i)).getSeconds();
            sumDuration.add(duration);
        }
        return sumDuration.stream().mapToLong(Long::longValue).sum();
    }


    public List<PassageDTO> getTodayPassages() {
        return gate.getPassages().stream()
                .filter(Passage::isAccepted)
                .map(p -> new PassageDTO(p.getNfcCardId(), p.getTimestamp(), gate.getGateId()))
                .toList();
    }

//    public List<PassageDTO> getAllPassagesOfToday(){
//        LocalDate today = LocalDate.now();
//        return gate.getPassages().stream()
//                .filter(p -> p.getTimestamp().toLocalDate()
//                        .isEqual(today))
//                .map(p -> new PassageDTO(p.getNfcCardId(), p.getTimestamp()))
//                .toList();
//    }
}
