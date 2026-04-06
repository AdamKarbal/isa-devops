package fr.univcotedazur.skimaster.integration;

import fr.univcotedazur.skimaster.admin.components.AdminService;
import fr.univcotedazur.skimaster.admin.components.DisplayAdminProxy;
import fr.univcotedazur.skimaster.gate.components.Portier;
import fr.univcotedazur.skimaster.incident.components.IncidentService;
import fr.univcotedazur.skimaster.incident.dto.IncidentDTO;
import fr.univcotedazur.skimaster.incident.entities.IncidentTargetType;
import fr.univcotedazur.skimaster.incident.entities.IncidentType;
import fr.univcotedazur.skimaster.incident.repositories.IncidentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class AdminDisplayIT {

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private IncidentRepository incidentRepository;

    @MockitoBean
    private Portier portier;

    @MockitoBean
    private DisplayAdminProxy displayAdminProxy;

    private final String TARGET_GATE = "gate-1";

    @BeforeEach
    public void setup() {
        incidentRepository.deleteAll();
    }

    @Test
    public void testGateManualCloseNotifiesAllDisplays() {
        adminService.setStatus(TARGET_GATE, "CLOSE");

        verify(displayAdminProxy, times(1)).sendMessage("Portique " + TARGET_GATE + " ferme", TARGET_GATE);
    }

    @Test
    public void testIncidentReportNotifiesAllDisplays() {
        IncidentDTO dto = new IncidentDTO(
                null,
                IncidentType.EQUIPMENT_FAILURE,
                "Anomalie detectee sur le portique",
                LocalDateTime.now(),
                IncidentTargetType.PORTIQUE,
                TARGET_GATE,
                false
        );

        incidentService.reportIncident(dto);

        verify(displayAdminProxy, times(1)).sendMessage(contains("[ALERTE]"), eq(TARGET_GATE));
    }

    @Test
    public void testResolveClosingIncidentOpensGateAndNotifiesDisplays() {
        IncidentDTO dto = new IncidentDTO(
                null,
                IncidentType.EQUIPMENT_FAILURE,
                "Moteur bloque par la neige",
                LocalDateTime.now(),
                IncidentTargetType.PORTIQUE,
                TARGET_GATE,
                true
        );

        incidentService.reportIncident(dto);

        adminService.resolveIncidentsForGate(TARGET_GATE);

        verify(portier, times(1)).openGate(TARGET_GATE);
        verify(displayAdminProxy, times(1)).clearForGate(TARGET_GATE);
        verify(displayAdminProxy, times(1)).sendMessage(contains("[RESOLUTION]"), eq(TARGET_GATE));
    }

    @Test
    public void testResolveNonClosingIncidentDoesNotOpenGate() {
        IncidentDTO dto = new IncidentDTO(
                null,
                IncidentType.EQUIPMENT_FAILURE,
                "Anomalie mineure sur le portique",
                LocalDateTime.now(),
                IncidentTargetType.PORTIQUE,
                TARGET_GATE,
                false
        );

        incidentService.reportIncident(dto);

        adminService.resolveIncidentsForGate(TARGET_GATE);

        verify(portier, never()).openGate(TARGET_GATE);
        verify(displayAdminProxy, times(1)).clearForGate(TARGET_GATE);
        verify(displayAdminProxy, times(1)).sendMessage(contains("[RESOLUTION]"), eq(TARGET_GATE));
    }
}
