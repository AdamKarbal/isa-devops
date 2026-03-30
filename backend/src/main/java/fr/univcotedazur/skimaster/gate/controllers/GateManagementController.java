package fr.univcotedazur.skimaster.gate.controllers;

import fr.univcotedazur.skimaster.gate.components.Portier;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateStatusResponseDTO;
import fr.univcotedazur.skimaster.gate.dtos.GateAlertThresholdRequestDTO;
import fr.univcotedazur.skimaster.gate.dtos.GateMonitoringStatusDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = GateManagementController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class GateManagementController {

    public static final String BASE_URI = "/gates";
    private final Portier portier;

    public GateManagementController(Portier portier) {
        this.portier = portier;
    }

    @GetMapping("/status")
    public ResponseEntity<List<GateMonitoringStatusDTO>> getAllStatuses() {
        List<GateMonitoringStatusDTO> statuses = portier.getConfiguredGateCodes().stream()
                .map(code -> toMonitoringStatus(portier.gateStatus(code), code))
                .toList();
        return ResponseEntity.ok(statuses);
    }

    @PutMapping("/{gateCode}/alert-threshold")
    public ResponseEntity<GateMonitoringStatusDTO> setAlertThreshold(@PathVariable String gateCode,
                                                                     @Valid @RequestBody GateAlertThresholdRequestDTO request) {
        portier.setAlertThreshold(gateCode, request.alertThreshold());
        return ResponseEntity.ok(toMonitoringStatus(portier.gateStatus(gateCode), gateCode));
    }

    private static GateMonitoringStatusDTO toMonitoringStatus(GateStatusResponseDTO status, String fallbackGateCode) {
        String gateId = (status.gateId() == null || status.gateId().isBlank()) ? fallbackGateCode : status.gateId();
        return new GateMonitoringStatusDTO(gateId, status.status(), status.alertThreshold());
    }
}
