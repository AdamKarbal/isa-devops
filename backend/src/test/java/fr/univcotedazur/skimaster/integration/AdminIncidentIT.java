package fr.univcotedazur.skimaster.integration;

import fr.univcotedazur.skimaster.admin.components.AdminService;
import fr.univcotedazur.skimaster.admin.exceptions.StillUnresolvedIncident;
import fr.univcotedazur.skimaster.gate.components.Portier;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class AdminIncidentIT {

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private IncidentRepository incidentRepository;

    @MockitoBean
    private Portier portier;

    //TODO: A revoir avec adamoStark 
    @MockitoBean
    private PortierInformation portierInformation;

    private final String TARGET_GATE = "gate-1";

    @BeforeEach
    public void setup(){
        incidentRepository.deleteAll();
    }

    @Test
    public void testIncidentReportAndAdminResolution(){

        //Le service reçoit normalement un dto déjà bon par le controller, c'est pour ça qu'il est fait à la main ici
        IncidentDTO dto = new IncidentDTO(
                null,
                IncidentType.EQUIPMENT_FAILURE,
                "Moteur bloqué par la neige",
                LocalDateTime.now(),
                IncidentTargetType.PORTIQUE,
                TARGET_GATE,
                true
        );

        incidentService.reportIncident(dto);

        assertFalse(incidentRepository.findByTargetCodeAndResolvedFalseAndClosesTrue(TARGET_GATE).isEmpty(),
                "La DB doit contenir l'incident avec un status non résolu et fermant.");

        verify(portier, times(1)).closeGate(TARGET_GATE);

        assertThrows(StillUnresolvedIncident.class, () -> {
            adminService.setStatus(TARGET_GATE, "OPEN");
        }, "Le système doit empêcher l'ouverture d'un portique ayant un incident non résolu.");

        verify(portier, never()).openGate(TARGET_GATE);

        adminService.resolveIncidentsForGate(TARGET_GATE);

        assertTrue(incidentRepository.findByTargetCodeAndResolvedFalseAndClosesTrue(TARGET_GATE).isEmpty(),
                "La DB d'incident ne doit maintenant plus contenir d'incidents non résolus pour " + TARGET_GATE + ".");

        verify(portier, times(1)).openGate(TARGET_GATE);
    }

}
