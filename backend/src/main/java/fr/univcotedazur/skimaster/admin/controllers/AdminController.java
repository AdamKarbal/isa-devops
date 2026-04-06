package fr.univcotedazur.skimaster.admin.controllers;

import fr.univcotedazur.skimaster.admin.components.AdminService;
import fr.univcotedazur.skimaster.admin.dto.AdminDisplayMessageDTO;
import fr.univcotedazur.skimaster.admin.dto.DisplayAlertDTO;
import fr.univcotedazur.skimaster.gate.dtos.GateAlertThresholdRequestDTO;
import fr.univcotedazur.skimaster.gate.dtos.GateMonitoringStatusDTO;
import fr.univcotedazur.skimaster.incident.entities.Incident;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = AdminController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class AdminController {
    public static final String BASE_URI = "/admin";

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/{code}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeGate(@PathVariable String code) {
        this.adminService.setStatus(code, "CLOSE");
    }

    @PostMapping("/{code}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void openGate(@PathVariable String code) {
        this.adminService.setStatus(code, "OPEN");
    }

    @PostMapping("/openAll")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void openAllGates() {
        this.adminService.openAllGates();
    }

    @PostMapping("/closeAll")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeAllGates() {
        this.adminService.closeAllGates();
    }

    @GetMapping("/alerts")
    @ResponseStatus(HttpStatus.CREATED)
    public List<String> getPendingIncidents() {
        List<String> incidents = new ArrayList<>();
        List<Incident> incidentList = this.adminService.getPendingIncidents();
        for (Incident incident : incidentList) {
            incidents.add(incident.toString());
        }
        return incidents;
    }

    @PostMapping("/resolve/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resolveIncidentsForGate(@PathVariable String code) {
        this.adminService.resolveIncidentsForGate(code);
    }

    @PostMapping("/display/message")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendMessageToDisplay(@Valid @RequestBody AdminDisplayMessageDTO request) {
        if (request.displayCode() == null || request.displayCode().isBlank()) {
            this.adminService.sendMessage(request.message());
            return;
        }
        this.adminService.sendMessage(request.message(), request.displayCode());
    }

    @DeleteMapping("/display/message")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearDisplay() {
        this.adminService.clearPannel();
    }

    @GetMapping("/display/alerts")
    public List<DisplayAlertDTO> getDisplayAlerts() {
        return this.adminService.getDisplayAlerts();
    }

    @GetMapping("/display/alerts/{displayCode}")
    public List<DisplayAlertDTO> getDisplayAlerts(@PathVariable String displayCode) {
        return this.adminService.getDisplayAlerts(displayCode);
    }

    @GetMapping("/display/list")
    public List<String> listDisplays() {
        return this.adminService.getDisplayList();
    }

    @GetMapping("/gates/status")
    public ResponseEntity<List<GateMonitoringStatusDTO>> getAllGateStatuses() {
        return ResponseEntity.ok(this.adminService.getAllGateStatuses());
    }

    @PutMapping("/gates/{gateCode}/alert-threshold")
    public ResponseEntity<GateMonitoringStatusDTO> setAlertThreshold(@PathVariable String gateCode,
                                                                     @Valid @RequestBody GateAlertThresholdRequestDTO request) {
        return ResponseEntity.ok(this.adminService.updateAlertThreshold(gateCode, request.alertThreshold()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ProblemDetail> handleDoorClientError(HttpClientErrorException e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(e.getStatusCode(), e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(pd);
    }
}
