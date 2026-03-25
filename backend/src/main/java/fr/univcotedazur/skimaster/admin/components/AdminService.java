package fr.univcotedazur.skimaster.admin.components;

import fr.univcotedazur.skimaster.admin.exceptions.StillUnresolvedIncident;
import fr.univcotedazur.skimaster.admin.interfaces.AdminOperations;
import fr.univcotedazur.skimaster.admin.dto.DisplayAlertDTO;
import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import fr.univcotedazur.skimaster.inbox.interfaces.MessageInput;
import fr.univcotedazur.skimaster.incident.components.IncidentService;
import fr.univcotedazur.skimaster.incident.entities.Incident;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class AdminService implements AdminOperations {

    private final ManualGateControl manualGateControl;
    private final MessageInput messageInput;
    private final IncidentService incidentService;
    private final DisplayAdminProxy displayAdminProxy;

    public AdminService(ManualGateControl manualGateControl,
                        MessageInput messageInput,
                        IncidentService incidentService,
                        DisplayAdminProxy displayAdminProxy) {
        this.manualGateControl = manualGateControl;
        this.messageInput = messageInput;
        this.incidentService = incidentService;
        this.displayAdminProxy = displayAdminProxy;
    }

    @Override
    public void setStatus(String code, String status) {
        if (Objects.equals(status, "OPEN")) {
            if (incidentService.hasUnresolvedClosingIncidents(code)) {
                throw new StillUnresolvedIncident("Le portique " + code + " a encore un incident fermant non résolu. Résoudre l'incident avant de réouvir.");
            }
            manualGateControl.openGate(code);

            this.incidentService.resolveIncidentsForGate(code);
        } else if (Objects.equals(status, "CLOSE")) {
            manualGateControl.closeGate(code);
        }
    }

    @Override
    public void sendMessage(String msg) {
        messageInput.sendMessage(msg);
    }

    public void sendMessage(String msg, String displayCode) {
        displayAdminProxy.sendMessage(msg, displayCode);
    }

    @Override
    public void clearPannel() {
        messageInput.clearPannel();
    }

    @Override
    public void openAllGates() {
        manualGateControl.openAllGates();
    }

    @Override
    public void closeAllGates() {
        manualGateControl.closeAllGates();
    }

    public List<Incident> getPendingIncidents(){
        return this.incidentService.findPendingIncidents();
    }

    public List<DisplayAlertDTO> getDisplayAlerts() {
        return displayAdminProxy.getAlerts();
    }

    public List<DisplayAlertDTO> getDisplayAlerts(String displayCode) {
        return displayAdminProxy.getAlerts(displayCode);
    }

    public List<String> getDisplayList() {
        return displayAdminProxy.getDisplayList();
    }

    @Override
    public void resolveIncidentsForGate(String code){
        this.incidentService.resolveIncidentsForGate(code);
        this.setStatus(code, "OPEN");
        displayAdminProxy.clearForGate(code);
        displayAdminProxy.sendMessage("[RESOLUTION] Portique " + code + " : incident resolu, portique de nouveau operationnel.", code);
    }
}
