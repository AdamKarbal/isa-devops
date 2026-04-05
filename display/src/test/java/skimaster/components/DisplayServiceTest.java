package skimaster.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skimaster.dtos.DisplayAlertRequestDTO;

import static org.junit.jupiter.api.Assertions.*;

class DisplayServiceTest {

    private DisplayService service;

    @BeforeEach
    void setUp() {
        service = new DisplayService("display-test");
    }

    // --- Stockage des messages ---

    @Test
    void shouldStoreAlertWithProvidedGateCode() {
        service.pushAlert(new DisplayAlertRequestDTO("Incident portique", "gate-1"));

        assertEquals(1, service.getAlerts().size());
        assertEquals("gate-1", service.getAlerts().getFirst().gateCode());
        assertEquals("Incident portique", service.getAlerts().getFirst().message());
    }

    @Test
    void shouldFallbackToAUCUNEWhenGateCodeIsNull() {
        service.pushAlert(new DisplayAlertRequestDTO("Message sans code", null));

        assertEquals("AUCUNE", service.getAlerts().getFirst().gateCode());
    }

    @Test
    void shouldFallbackToAUCUNEWhenGateCodeIsBlank() {
        service.pushAlert(new DisplayAlertRequestDTO("Message sans code", ""));

        assertEquals("AUCUNE", service.getAlerts().getFirst().gateCode());
    }

    @Test
    void shouldStoreTimestampOnAlert() {
        service.pushAlert(new DisplayAlertRequestDTO("Test", "gate-1"));

        assertNotNull(service.getAlerts().getFirst().receivedAt());
    }

    // --- Accumulation et ordre ---

    @Test
    void shouldAccumulateMultipleAlerts() {
        service.pushAlert(new DisplayAlertRequestDTO("Message 1", "gate-1"));
        service.pushAlert(new DisplayAlertRequestDTO("Message 2", "gate-2"));
        service.pushAlert(new DisplayAlertRequestDTO("Message 3", "gate-1"));

        assertEquals(3, service.getAlerts().size());
    }

    @Test
    void shouldPreserveInsertionOrder() {
        service.pushAlert(new DisplayAlertRequestDTO("Premier", "gate-1"));
        service.pushAlert(new DisplayAlertRequestDTO("Deuxieme", "gate-2"));

        assertEquals("Premier", service.getAlerts().get(0).message());
        assertEquals("Deuxieme", service.getAlerts().get(1).message());
    }

    // --- Filtrage par gateCode ---

    @Test
    void shouldReturnAlertsForSpecificGateCode() {
        service.pushAlert(new DisplayAlertRequestDTO("Alerte gate-1", "gate-1"));
        service.pushAlert(new DisplayAlertRequestDTO("Alerte gate-2", "gate-2"));
        service.pushAlert(new DisplayAlertRequestDTO("Alerte gate-1 bis", "gate-1"));

        var result = service.getAlertsForGateCode("gate-1");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(a -> a.gateCode().equals("gate-1")));
    }

    @Test
    void shouldMatchGateCodeCaseInsensitively() {
        service.pushAlert(new DisplayAlertRequestDTO("Test", "GATE-1"));

        var result = service.getAlertsForGateCode("gate-1");

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoAlertsForGateCode() {
        service.pushAlert(new DisplayAlertRequestDTO("Alerte gate-2", "gate-2"));

        var result = service.getAlertsForGateCode("gate-1");

        assertTrue(result.isEmpty());
    }

    // --- Effacement global ---

    @Test
    void shouldClearAllAlerts() {
        service.pushAlert(new DisplayAlertRequestDTO("Message 1", "gate-1"));
        service.pushAlert(new DisplayAlertRequestDTO("Message 2", "gate-2"));

        service.clearAlerts();

        assertEquals(0, service.getAlerts().size());
    }

    // --- Effacement par portique ---

    @Test
    void shouldClearAlertsForSpecificGateOnly() {
        service.pushAlert(new DisplayAlertRequestDTO("Alerte gate-1", "gate-1"));
        service.pushAlert(new DisplayAlertRequestDTO("Alerte gate-2", "gate-2"));

        service.clearAlertsForGateCode("gate-1");

        assertEquals(1, service.getAlerts().size());
        assertEquals("gate-2", service.getAlerts().getFirst().gateCode());
    }

    @Test
    void shouldNotRemoveOtherAlertsWhenClearingByGateCode() {
        service.pushAlert(new DisplayAlertRequestDTO("Admin global", "ADMIN"));
        service.pushAlert(new DisplayAlertRequestDTO("Alerte gate-1", "gate-1"));

        service.clearAlertsForGateCode("gate-1");

        assertEquals(1, service.getAlerts().size());
        assertEquals("ADMIN", service.getAlerts().getFirst().gateCode());
    }

    // --- Codes distincts triés ---

    @Test
    void shouldReturnDistinctGateCodesSorted() {
        service.pushAlert(new DisplayAlertRequestDTO("A", "gate-3"));
        service.pushAlert(new DisplayAlertRequestDTO("B", "gate-1"));
        service.pushAlert(new DisplayAlertRequestDTO("C", "gate-3"));
        service.pushAlert(new DisplayAlertRequestDTO("D", "gate-2"));

        var codes = service.getDistinctDisplayCodes();

        assertEquals(3, codes.size());
        assertEquals("gate-1", codes.get(0));
        assertEquals("gate-2", codes.get(1));
        assertEquals("gate-3", codes.get(2));
    }

    // --- Identite du display ---

    @Test
    void shouldReturnDisplayId() {
        assertEquals("display-test", service.getDisplayId());
    }

    // --- Contenu des messages de fermeture automatique ---

    @Test
    void shouldStoreAutoCloseMessageContent() {
        service.pushAlert(new DisplayAlertRequestDTO(
                "Seuil d'affluence atteint (3 passages/heure) - portique gate-1 ferme automatiquement",
                "gate-1"
        ));

        var alert = service.getAlerts().getFirst();
        assertTrue(alert.message().contains("Seuil d'affluence atteint"));
        assertTrue(alert.message().contains("gate-1"));
        assertEquals("gate-1", alert.gateCode());
    }
}
