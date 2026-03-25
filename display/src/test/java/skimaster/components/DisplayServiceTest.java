package skimaster.components;

import org.junit.jupiter.api.Test;
import skimaster.dtos.DisplayAlertRequestDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DisplayServiceTest {

    @Test
    void shouldStoreAlertAndFallbackGateCodeWhenMissing() {
        DisplayService service = new DisplayService();

        service.pushAlert(new DisplayAlertRequestDTO("Incident test", ""));

        assertEquals(1, service.getAlerts().size());
        assertEquals("Incident test", service.getAlerts().getFirst().message());
        assertEquals("AUCUNE", service.getAlerts().getFirst().gateCode());
    }

    @Test
    void shouldStoreProvidedGateCodeAndTimestamp() {
        DisplayService service = new DisplayService();

        service.pushAlert(new DisplayAlertRequestDTO("Incident portique", "G-NORD-01"));

        assertEquals(1, service.getAlerts().size());
        assertEquals("G-NORD-01", service.getAlerts().getFirst().gateCode());
        assertNotNull(service.getAlerts().getFirst().receivedAt());
    }

    @Test
    void shouldClearStoredAlerts() {
        DisplayService service = new DisplayService();

        service.pushAlert(new DisplayAlertRequestDTO("Message admin", "ADMIN"));
        service.clearAlerts();

        assertEquals(0, service.getAlerts().size());
    }

    @Test
    void shouldReturnAlertsForSpecificGateCode() {
        DisplayService service = new DisplayService();

        service.pushAlert(new DisplayAlertRequestDTO("Alerte 1", "gate-1"));
        service.pushAlert(new DisplayAlertRequestDTO("Alerte 2", "gate-2"));

        assertEquals(1, service.getAlertsForGateCode("gate-1").size());
        assertEquals("Alerte 1", service.getAlertsForGateCode("gate-1").getFirst().message());
    }
}
