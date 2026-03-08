package fr.univcotedazur.skimaster.incident.repositories;

import fr.univcotedazur.skimaster.incident.entities.Incident;
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
        Incident meteoIncident = new Incident(IncidentType.WEATHER, "Tempête de neige");
        Incident materielIncident = new Incident(IncidentType.EQUIPMENT_FAILURE, "Télésiège en panne");

        incidentRepository.save(meteoIncident);
        incidentRepository.save(materielIncident);

        List<Incident> result = incidentRepository.findByType(IncidentType.WEATHER);

        assertEquals(1, result.size());
        assertEquals("Tempête de neige", result.get(0).getDescription());
    }

    @Test
    void shouldFindFirstByOrderByTimeDesc() throws InterruptedException {
        Incident oldIncident = new Incident(IncidentType.EQUIPMENT_FAILURE, "Ancien");
        incidentRepository.save(oldIncident);

        Thread.sleep(50);

        Incident recentIncident = new Incident(IncidentType.WEATHER, "Récent");
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
