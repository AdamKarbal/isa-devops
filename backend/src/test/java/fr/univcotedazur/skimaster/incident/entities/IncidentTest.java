package fr.univcotedazur.skimaster.incident.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class IncidentTest {

    @Test
    void shouldCreateIncidentWithCorrectValues() {
        Incident incident = new Incident(IncidentType.AVALANCHE, IncidentTargetType.PISTE, "P-1", "Avalanche", false);

        assertEquals(IncidentType.AVALANCHE, incident.getType());
        assertEquals("Avalanche", incident.getDescription());
        assertNotNull(incident.getTime());
        assertNull(incident.getId());
        assertFalse(incident.isClosing());
    }

    @Test
    void equalsAndHashCodeShouldWorkBasedOnBusinessKey() {
        LocalDateTime now = LocalDateTime.now();

        Incident incident1 = new Incident(IncidentType.AVALANCHE, IncidentTargetType.PISTE, "P-1", "Coulée", now, false);
        Incident incident2 = new Incident(IncidentType.AVALANCHE, IncidentTargetType.PISTE, "P-1", "Autre description on s'en fiche", now, false);

        assertEquals(incident1, incident2);
        assertEquals(incident1.hashCode(), incident2.hashCode());
    }
}
