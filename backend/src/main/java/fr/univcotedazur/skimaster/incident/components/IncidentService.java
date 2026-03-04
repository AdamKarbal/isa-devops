package fr.univcotedazur.skimaster.incident.components;

import fr.univcotedazur.skimaster.incident.entities.Incident;
import fr.univcotedazur.skimaster.incident.entities.IncidentType;
import fr.univcotedazur.skimaster.incident.entities.IncidentTargetType;
import fr.univcotedazur.skimaster.incident.dto.IncidentDTO;
import fr.univcotedazur.skimaster.incident.interfaces.DisplayNotifier;
import fr.univcotedazur.skimaster.incident.repositories.IncidentRepository;
import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    private final IncidentRepository repository;
    private final ManualGateControl manualGateControl;
    private final DisplayNotifier displayNotifier;

    public IncidentService(IncidentRepository repository, ManualGateControl manualGateControl, DisplayNotifier displayNotifier) {
        this.repository = repository;
        this.manualGateControl = manualGateControl;
        this.displayNotifier = displayNotifier;
    }

    public Optional<Incident> findFirstByOrderByTimeDesc() {
        return repository.findFirstByOrderByTimeDesc();
    }

    @Transactional
    public void saveIncident(Incident incident){
        repository.save(incident);
    }

    @Transactional
    public void reportIncident(IncidentDTO dto) {
        IncidentTargetType targetType = dto.targetType() == null ? IncidentTargetType.PISTE : dto.targetType();
        String targetCode = (dto.targetCode() == null || dto.targetCode().isBlank()) ? "UNKNOWN" : dto.targetCode();
        Incident incident = new Incident(dto.type(), targetType, targetCode, dto.description(), dto.time());
        repository.save(incident);

        if (targetType == IncidentTargetType.PORTIQUE && !"UNKNOWN".equals(targetCode)) {
            manualGateControl.closeGate(targetCode);
        }

        displayNotifier.publishIncidentAlert(buildDisplayMessage(incident), getGateCodeForDisplay(incident));
    }

    @Transactional
    public List<Incident> findAllIncidents(){
        return repository.findAll();
    }

    @Transactional
    public List<Incident> findIncidentByType(IncidentType type){
        return repository.findByType(type);
    }

    private String buildDisplayMessage(Incident incident) {
        return "[ALERTE] " + incident.getType() + " sur " + incident.getTargetType() + " " + incident.getTargetCode() + " : " + incident.getDescription();
    }

    private String getGateCodeForDisplay(Incident incident) {
        if (incident.getTargetType() == IncidentTargetType.PORTIQUE) {
            return incident.getTargetCode();
        }
        return "AUCUNE";
    }
}
