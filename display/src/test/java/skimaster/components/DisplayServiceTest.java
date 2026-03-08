package skimaster.components;

import org.junit.jupiter.api.Test;
import skimaster.dtos.DisplayAlertRequestDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisplayServiceTest {

    @Test
    void shouldStoreAlertAndFallbackGateCodeWhenMissing() {
        DisplayService service = new DisplayService();

        service.pushAlert(new DisplayAlertRequestDTO("Incident test", ""));

        assertEquals(1, service.getAlerts().size());
        assertEquals("Incident test", service.getAlerts().getFirst().message());
        assertEquals("AUCUNE", service.getAlerts().getFirst().gateCode());
    }
}
