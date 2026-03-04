package fr.univcotedazur.skimaster.incident.components;

import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import fr.univcotedazur.skimaster.incident.dto.IncidentDTO;
import fr.univcotedazur.skimaster.incident.entities.Incident;
import fr.univcotedazur.skimaster.incident.entities.IncidentTargetType;
import fr.univcotedazur.skimaster.incident.entities.IncidentType;
import fr.univcotedazur.skimaster.incident.interfaces.DisplayNotifier;
import fr.univcotedazur.skimaster.incident.repositories.IncidentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static fr.univcotedazur.skimaster.incident.entities.IncidentType.AVALANCHE;
import static fr.univcotedazur.skimaster.incident.entities.IncidentType.WEATHER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private ManualGateControl manualGateControl;

    @Mock
    private DisplayNotifier displayNotifier;

    @InjectMocks
    private IncidentService incidentService;

    private Incident mockIncident;
    private final IncidentType mockType = WEATHER;
    private final IncidentType mockType2 = AVALANCHE;

    @BeforeEach
    void setUp() {
        mockIncident = new Incident(mockType, IncidentTargetType.PISTE, "P-1", "Piste verglacee", LocalDateTime.now());
    }

    @Test
    void shouldReturnFirstIncidentOrderedByTimeDesc() {
        when(incidentRepository.findFirstByOrderByTimeDesc()).thenReturn(Optional.of(mockIncident));

        Optional<Incident> result = incidentService.findFirstByOrderByTimeDesc();

        assertTrue(result.isPresent());
        assertEquals(mockIncident, result.get());
        verify(incidentRepository, times(1)).findFirstByOrderByTimeDesc();
    }

    @Test
    void shouldSaveIncident() {
        incidentService.saveIncident(mockIncident);

        verify(incidentRepository, times(1)).save(mockIncident);
    }

    @Test
    void shouldFindAllIncidents() {
        List<Incident> expectedIncidents = Arrays.asList(
                mockIncident,
                new Incident(mockType2, "Avalanche secteur nord")
        );
        when(incidentRepository.findAll()).thenReturn(expectedIncidents);

        List<Incident> result = incidentService.findAllIncidents();

        assertEquals(2, result.size());
        assertEquals(expectedIncidents, result);
        verify(incidentRepository, times(1)).findAll();
    }

    @Test
    void shouldFindIncidentsByType() {
        List<Incident> expectedIncidents = List.of(mockIncident);
        when(incidentRepository.findByType(any())).thenReturn(expectedIncidents);

        List<Incident> result = incidentService.findIncidentByType(mockType);

        assertFalse(result.isEmpty());
        assertEquals(expectedIncidents, result);
        verify(incidentRepository, times(1)).findByType(any());
    }

    @Test
    void shouldReportIncidentAndCloseTargetGateWhenTargetIsPortique() {
        IncidentDTO dto = new IncidentDTO(
                null,
                IncidentType.EQUIPMENT_FAILURE,
                "Portique en panne",
                LocalDateTime.now(),
                IncidentTargetType.PORTIQUE,
                "G-NORD-01"
        );

        incidentService.reportIncident(dto);

        verify(incidentRepository, times(1)).save(any(Incident.class));
        verify(manualGateControl, times(1)).closeGate("G-NORD-01");
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> gateCodeCaptor = ArgumentCaptor.forClass(String.class);
        verify(displayNotifier, times(1)).publishIncidentAlert(messageCaptor.capture(), gateCodeCaptor.capture());
        assertEquals("G-NORD-01", gateCodeCaptor.getValue());
        assertTrue(messageCaptor.getValue().contains("EQUIPMENT_FAILURE"));
        assertTrue(messageCaptor.getValue().contains("PORTIQUE G-NORD-01"));
    }

    @Test
    void shouldReportIncidentWithoutClosingGateWhenTargetIsPiste() {
        IncidentDTO dto = new IncidentDTO(
                null,
                IncidentType.OBSTACLE,
                "Obstacle piste verte",
                LocalDateTime.now(),
                IncidentTargetType.PISTE,
                "P-VERTE-2"
        );

        incidentService.reportIncident(dto);

        verify(incidentRepository, times(1)).save(any(Incident.class));
        verify(manualGateControl, never()).closeGate(any(String.class));
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> gateCodeCaptor = ArgumentCaptor.forClass(String.class);
        verify(displayNotifier, times(1)).publishIncidentAlert(messageCaptor.capture(), gateCodeCaptor.capture());
        assertEquals("AUCUNE", gateCodeCaptor.getValue());
        assertTrue(messageCaptor.getValue().contains("OBSTACLE"));
        assertTrue(messageCaptor.getValue().contains("PISTE P-VERTE-2"));
    }
}
