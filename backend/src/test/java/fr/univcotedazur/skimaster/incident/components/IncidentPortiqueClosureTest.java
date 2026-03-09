package fr.univcotedazur.skimaster.incident.components;

import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import fr.univcotedazur.skimaster.incident.dto.IncidentDTO;
import fr.univcotedazur.skimaster.incident.entities.Incident;
import fr.univcotedazur.skimaster.incident.entities.IncidentTargetType;
import fr.univcotedazur.skimaster.incident.entities.IncidentType;
import fr.univcotedazur.skimaster.incident.interfaces.DisplayNotifier;
import fr.univcotedazur.skimaster.incident.repositories.IncidentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IncidentPortiqueClosureTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private ManualGateControl manualGateControl;

    @Mock
    private DisplayNotifier displayNotifier;

    @InjectMocks
    private IncidentService incidentService;

    @Test
    void shouldCloseTheSpecificGateDeclaredInIncident() {
        IncidentDTO incident = new IncidentDTO(
                null,
                IncidentType.EQUIPMENT_FAILURE,
                "Portique bloque",
                LocalDateTime.now(),
                IncidentTargetType.PORTIQUE,
                "gate-2"
        );

        System.out.println("Declaring incident for gate: " + incident.targetCode());
        incidentService.reportIncident(incident);
        System.out.println("Expected closed gate: " + incident.targetCode());

        verify(incidentRepository, times(1)).save(any(Incident.class));
        verify(manualGateControl, times(1)).closeGate("gate-2");
    }
}
