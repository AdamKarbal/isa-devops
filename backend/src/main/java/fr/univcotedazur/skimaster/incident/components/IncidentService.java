package fr.univcotedazur.skimaster.incident.components;

import fr.univcotedazur.skimaster.incident.entities.Incident;
import fr.univcotedazur.skimaster.incident.entities.IncidentType;
import fr.univcotedazur.skimaster.incident.entities.IncidentTargetType;
import fr.univcotedazur.skimaster.incident.dto.IncidentDTO;
import fr.univcotedazur.skimaster.incident.interfaces.DisplayNotifier;
import fr.univcotedazur.skimaster.incident.repositories.IncidentRepository;
import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import jakarta.transaction.Transactional;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForCalendar;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    private final IncidentRepository repository;
    private final ManualGateControl manualGateControl;
    private final DisplayNotifier displayNotifier;
    private final FutureOrPresentValidatorForCalendar futureOrPresentValidatorForCalendar;

    public IncidentService(IncidentRepository repository, ManualGateControl manualGateControl, DisplayNotifier displayNotifier, FutureOrPresentValidatorForCalendar futureOrPresentValidatorForCalendar) {
        this.repository = repository;
        this.manualGateControl = manualGateControl;
        this.displayNotifier = displayNotifier;
        this.futureOrPresentValidatorForCalendar = futureOrPresentValidatorForCalendar;
    }

    public Optional<Incident> findFirstByOrderByTimeDesc() {
        return repository.findFirstByOrderByTimeDesc();
    }

    @Transactional
    public void reportIncident(IncidentDTO dto) {
        IncidentTargetType targetType = dto.targetType() == null ? IncidentTargetType.PISTE : dto.targetType();
        String targetCode = (dto.targetCode() == null || dto.targetCode().isBlank()) ? "UNKNOWN" : dto.targetCode();
        Incident incident = new Incident(dto.type(), targetType, targetCode, dto.description(), dto.time(), dto.closes());
        repository.save(incident);

        if (targetType == IncidentTargetType.PORTIQUE && !"UNKNOWN".equals(targetCode) && Boolean.TRUE.equals(incident.isClosing())) {
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

    @Transactional
    public boolean hasUnresolvedClosingIncidents(String code) {
        return !repository.findByTargetCodeAndResolvedFalseAndClosesTrue(code).isEmpty();
    }

    @Transactional
    public List<Incident> findPendingIncidents(){
        return repository.findByResolvedFalse();
    }

    @Transactional
    public boolean hasUnresolvedIncidents(String code) {
        return !repository.findByTargetCodeAndResolvedFalse(code).isEmpty();
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

    @Transactional
    public Boolean resolveIncidentsForGate(String targetCode){
        List<Incident> unresolved = repository.findByTargetCodeAndResolvedFalse(targetCode);
        for (Incident incident : unresolved) {
            incident.resolve();
        }
        return repository.findByTargetCodeAndResolvedFalse(targetCode).isEmpty();
    }
}
