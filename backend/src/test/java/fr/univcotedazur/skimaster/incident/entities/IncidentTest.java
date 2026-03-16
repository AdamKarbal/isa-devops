package fr.univcotedazur.skimaster.incident.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IncidentTest {

    @Test
    void shouldCreateIncidentWithCorrectValues() {
        // Arrange & Act
        Incident incident = new Incident(IncidentType.AVALANCHE, "Piste bloquée");

        // Assert
        assertEquals(IncidentType.AVALANCHE, incident.getType());
        assertEquals("Piste bloquée", incident.getDescription());
        assertNotNull(incident.getTime());
        assertNull(incident.getId());
    }

    @Test
    void equalsAndHashCodeShouldWorkBasedOnTime() throws InterruptedException {
        Incident incident1 = new Incident(IncidentType.AVALANCHE, "Desc 1");

        Thread.sleep(10);

        Incident incident2 = new Incident(IncidentType.INJURY, "Desc 2");

        assertNotEquals(incident1, incident2);
        assertNotEquals(incident1.hashCode(), incident2.hashCode());

        assertEquals(incident1, incident1);
    }
}
