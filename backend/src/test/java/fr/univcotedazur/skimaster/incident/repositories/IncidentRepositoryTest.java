package fr.univcotedazur.skimaster.incident.repositories;

import fr.univcotedazur.skimaster.incident.entities.Incident;
import fr.univcotedazur.skimaster.incident.entities.IncidentTargetType;
import fr.univcotedazur.skimaster.incident.entities.IncidentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class IncidentRepositoryTest {

    @Autowired
    private IncidentRepository incidentRepository;

    @Test
    void shouldFindByType() {
        Incident meteoIncident = new Incident(IncidentType.WEATHER, IncidentTargetType.PISTE, "P-1", "Tempête de neige", false);
        Incident materielIncident = new Incident(IncidentType.EQUIPMENT_FAILURE, IncidentTargetType.PORTIQUE, "gate-1", "Tourniquet cassé", true);

        incidentRepository.save(meteoIncident);
        incidentRepository.save(materielIncident);

        List<Incident> result = incidentRepository.findByType(IncidentType.WEATHER);

        assertEquals(1, result.size());
        assertEquals("Tempête de neige", result.get(0).getDescription());
    }

    @Test
    void shouldFindFirstByOrderByTimeDesc() throws InterruptedException {
        Incident oldIncident = new Incident(IncidentType.WEATHER, IncidentTargetType.PISTE, "P-1", "Tempête de neige", false);
        incidentRepository.save(oldIncident);

        Thread.sleep(50);

        Incident recentIncident = new Incident(IncidentType.EQUIPMENT_FAILURE, IncidentTargetType.PORTIQUE, "gate-1", "Tourniquet cassé", true);
        incidentRepository.save(recentIncident);

        Optional<Incident> result = incidentRepository.findFirstByOrderByTimeDesc();

        assertTrue(result.isPresent());
        assertEquals("Récent", result.get().getDescription());
    }

    @Test
    void shouldReturnEmptyWhenDatabaseIsEmpty() {
        Optional<Incident> result = incidentRepository.findFirstByOrderByTimeDesc();

        assertTrue(result.isEmpty());
    }
}
