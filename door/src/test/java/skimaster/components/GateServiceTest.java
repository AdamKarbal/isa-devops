package skimaster.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skimaster.dtos.PassageRequestDTO;
import skimaster.entities.GateStatus;
import skimaster.exceptions.AlreadyClosedGateException;
import skimaster.exceptions.AlreadyOpenGateException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GateServiceTest {

    @Mock
    private DisplayNotifierClient displayNotifierClient;

    private GateService gateService;

    private static final Long CARD_ID = 1234567890L;

    @BeforeEach
    void setUp() {
        gateService = new GateService("gate-test", 3, displayNotifierClient);
        gateService.updateWhitelist(List.of(CARD_ID));
    }

    // --- Passages : acceptés / refusés ---

    @Test
    void shouldAcceptPassageWhenGateOpenAndCardWhitelisted() {
        var response = gateService.processPassage(new PassageRequestDTO(true, CARD_ID));

        assertTrue(response.isPresent());
        assertTrue(response.get().accepted());
    }

    @Test
    void shouldRefusePassageWhenCardNotWhitelisted() {
        var response = gateService.processPassage(new PassageRequestDTO(true, 9999L));

        assertTrue(response.isEmpty());
    }

    @Test
    void shouldRefusePassageWhenGateClosed() {
        gateService.closeGate();
        clearInvocations(displayNotifierClient);

        var response = gateService.processPassage(new PassageRequestDTO(true, CARD_ID));

        assertTrue(response.isEmpty());
    }

    // --- Fermeture automatique au seuil (passages/heure) ---

    @Test
    void shouldAutoCloseGateWhenThresholdReached() {
        passNTimes(2);
        assertEquals(GateStatus.OPEN, gateService.getStatus().status());

        gateService.processPassage(new PassageRequestDTO(true, CARD_ID));

        assertEquals(GateStatus.CLOSED, gateService.getStatus().status());
    }

    @Test
    void shouldNotifyDisplayWhenAutoCloseTriggered() {
        passNTimes(3);

        verify(displayNotifierClient).publishAlert(
            contains("Seuil d'affluence atteint"),
            eq("gate-test")
        );
    }

    @Test
    void shouldNotNotifyDisplayWhenPassageBelowThreshold() {
        passNTimes(2);

        verify(displayNotifierClient, never()).publishAlert(anyString(), anyString());
    }

    @Test
    void shouldNotifyDisplayExactlyOnceWhenThresholdReached() {
        passNTimes(3);

        verify(displayNotifierClient, times(1)).publishAlert(anyString(), anyString());
    }

    @Test
    void shouldNotAcceptMorePassagesAfterAutoClose() {
        passNTimes(3);

        var response = gateService.processPassage(new PassageRequestDTO(true, CARD_ID));

        assertTrue(response.isEmpty());
    }

    // --- currentPassages reflète la fenêtre d'une heure ---

    @Test
    void shouldReturnCurrentPassagesCountWithinHour() {
        passNTimes(2);

        assertEquals(2, gateService.getStatus().currentPassages());
    }

    // --- Fermeture / ouverture manuelle ---

    @Test
    void shouldCloseGateManually() {
        gateService.closeGate();

        assertEquals(GateStatus.CLOSED, gateService.getStatus().status());
    }

    @Test
    void shouldNotifyDisplayOnManualClose() {
        gateService.closeGate();

        verify(displayNotifierClient).publishAlert(contains("ferme"), eq("gate-test"));
    }

    @Test
    void shouldOpenGateAfterManualClose() {
        gateService.closeGate();
        gateService.openGate();

        assertEquals(GateStatus.OPEN, gateService.getStatus().status());
    }

    @Test
    void shouldThrowWhenClosingAlreadyClosedGate() {
        gateService.closeGate();

        assertThrows(AlreadyClosedGateException.class, () -> gateService.closeGate());
    }

    @Test
    void shouldThrowWhenOpeningAlreadyOpenGate() {
        assertThrows(AlreadyOpenGateException.class, () -> gateService.openGate());
    }

    // --- Alert threshold ---

    @Test
    void shouldReflectAlertThresholdInStatus() {
        assertEquals(3, gateService.getStatus().alertThreshold());
    }

    // --- Helpers ---

    private void passNTimes(int n) {
        for (int i = 0; i < n; i++) {
            gateService.processPassage(new PassageRequestDTO(true, CARD_ID));
        }
    }
}
