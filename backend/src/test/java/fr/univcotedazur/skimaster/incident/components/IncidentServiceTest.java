package fr.univcotedazur.skimaster.incident.components;

import fr.univcotedazur.skimaster.incident.entities.Incident;
import fr.univcotedazur.skimaster.incident.entities.IncidentType;
import fr.univcotedazur.skimaster.incident.repositories.IncidentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static fr.univcotedazur.skimaster.incident.entities.IncidentType.AVALANCHE;
import static fr.univcotedazur.skimaster.incident.entities.IncidentType.WEATHER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @InjectMocks
    private IncidentService incidentService;

    private Incident mockIncident;
    private final IncidentType MOCK_TYPE = WEATHER;
    private final IncidentType MOCK_TYPE2 = AVALANCHE;

    @BeforeEach
    void setUp() {
        mockIncident = new Incident(MOCK_TYPE, "Piste verglacée");
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
                new Incident(MOCK_TYPE2, "Avalanche secteur nord")
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

        List<Incident> result = incidentService.findIncidentByType(MOCK_TYPE);

        assertFalse(result.isEmpty());
        assertEquals(expectedIncidents, result);
        verify(incidentRepository, times(1)).findByType(any());
    }
}
