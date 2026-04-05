package skimaster.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class GateTest {

    private Gate gate;

    @BeforeEach
    void setUp() {
        gate = new Gate("gate-test", GateStatus.OPEN, 3, LocalTime.of(7, 0), LocalTime.of(17, 30));
    }

    // --- Alert threshold : comptage par heure ---

    @Test
    void shouldCountOnlyPassagesWithinLastHour() {
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now().minusMinutes(30)));
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now().minusMinutes(10)));

        assertEquals(2, gate.getCurrentPassages());
    }

    @Test
    void shouldNotCountPassagesOlderThanOneHour() {
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now().minusHours(2)));
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now().minusHours(1).minusSeconds(1)));

        assertEquals(0, gate.getCurrentPassages());
    }

    @Test
    void shouldIgnoreOldPassagesAndCountOnlyRecentOnes() {
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now().minusHours(2)));
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now().minusMinutes(30)));
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now().minusMinutes(5)));

        assertEquals(2, gate.getCurrentPassages());
    }

    @Test
    void shouldNotCountRefusedPassages() {
        gate.registerPassage(new Passage(1L, false, LocalDateTime.now().minusMinutes(10)));
        gate.registerPassage(new Passage(1L, false, LocalDateTime.now().minusMinutes(5)));

        assertEquals(0, gate.getCurrentPassages());
    }

    @Test
    void shouldCountOnlyAcceptedPassagesAmongMixed() {
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now().minusMinutes(20)));
        gate.registerPassage(new Passage(2L, false, LocalDateTime.now().minusMinutes(10)));
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now().minusMinutes(5)));

        assertEquals(2, gate.getCurrentPassages());
    }

    // --- Fermeture automatique au seuil ---

    @Test
    void shouldAutoCloseWhenThresholdReached() {
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));
        assertEquals(GateStatus.OPEN, gate.getStatus());

        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));

        assertEquals(GateStatus.CLOSED, gate.getStatus());
    }

    @Test
    void shouldNotAutoCloseWhenBelowThreshold() {
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));

        assertEquals(GateStatus.OPEN, gate.getStatus());
    }

    @Test
    void shouldNotAutoCloseOnRefusedPassages() {
        gate.registerPassage(new Passage(1L, false, LocalDateTime.now()));
        gate.registerPassage(new Passage(1L, false, LocalDateTime.now()));
        gate.registerPassage(new Passage(1L, false, LocalDateTime.now()));
        gate.registerPassage(new Passage(1L, false, LocalDateTime.now()));

        assertEquals(GateStatus.OPEN, gate.getStatus());
    }

    @Test
    void shouldNotAutoCloseOnOldPassagesEvenIfAboveThreshold() {
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now().minusHours(2)));
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now().minusHours(2)));
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now().minusHours(2)));

        assertEquals(GateStatus.OPEN, gate.getStatus());
    }

    // --- Mise à jour du seuil ---

    @Test
    void shouldUpdateAlertThreshold() {
        gate.setAlertThreshold(10);

        assertEquals(10, gate.getAlertThreshold());
    }

    @Test
    void shouldAutoCloseImmediatelyWhenThresholdSetToOne() {
        gate.setAlertThreshold(1);

        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));

        assertEquals(GateStatus.CLOSED, gate.getStatus());
    }

    // --- Décharge de la fenêtre glissante après une heure ---

    @Test
    void shouldDischargeExpiredPassagesAndRestartCycleFromZero() {
        // Phase 1 : 3 passages il y a plus d'une heure — hors fenêtre, compteur = 0, porte reste ouverte
        LocalDateTime plusDeUneHeure = LocalDateTime.now().minusHours(1).minusSeconds(30);
        gate.registerPassage(new Passage(1L, true, plusDeUneHeure));
        gate.registerPassage(new Passage(1L, true, plusDeUneHeure));
        gate.registerPassage(new Passage(1L, true, plusDeUneHeure));

        assertEquals(0, gate.getCurrentPassages());
        assertEquals(GateStatus.OPEN, gate.getStatus());

        // Phase 2 : nouveaux passages dans la fenêtre courante — seuls ceux-ci comptent
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));
        assertEquals(1, gate.getCurrentPassages());
        assertEquals(GateStatus.OPEN, gate.getStatus());

        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));
        assertEquals(2, gate.getCurrentPassages());
        assertEquals(GateStatus.OPEN, gate.getStatus());

        // Phase 3 : seuil atteint uniquement sur les passages récents → fermeture automatique
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));
        assertEquals(GateStatus.CLOSED, gate.getStatus());
    }

    @Test
    void shouldNotSumOldAndNewPassagesTowardsThreshold() {
        // 2 passages récents + 5 passages > 1h : le total "brut" dépasse le seuil de 3
        // mais seuls les 2 récents comptent → porte reste ouverte
        LocalDateTime anciens = LocalDateTime.now().minusHours(2);
        for (int i = 0; i < 5; i++) {
            gate.registerPassage(new Passage(1L, true, anciens));
        }
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));

        assertEquals(2, gate.getCurrentPassages());
        assertEquals(GateStatus.OPEN, gate.getStatus());
    }

    @Test
    void shouldReopenAndImmediatelyCloseAgainIfRecentPassagesStillWithinWindow() {
        // Si la porte est rouverte dans la même fenêtre horaire, les passages précédents
        // comptent toujours : le prochain passage atteint immédiatement le seuil
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));
        assertEquals(GateStatus.CLOSED, gate.getStatus());

        // Réouverture manuelle dans la même heure
        gate.setStatus(GateStatus.OPEN);

        // Les 3 passages récents sont toujours dans la fenêtre (currentPassages = 3 ≥ seuil)
        // Le prochain passage déclenche immédiatement une nouvelle fermeture
        gate.registerPassage(new Passage(1L, true, LocalDateTime.now()));
        assertEquals(GateStatus.CLOSED, gate.getStatus());
    }

    // --- Statut manuel ---

    @Test
    void shouldChangeStatusManually() {
        gate.setStatus(GateStatus.CLOSED);
        assertEquals(GateStatus.CLOSED, gate.getStatus());

        gate.setStatus(GateStatus.OPEN);
        assertEquals(GateStatus.OPEN, gate.getStatus());
    }

    // --- Incidents ---

    @Test
    void shouldRegisterIncident() {
        gate.registerIncident(new Incident(IncidentType.BREAKDOWN, "Panne moteur", java.time.Instant.now()));

        assertEquals(1, gate.getIncidents().size());
        assertEquals(IncidentType.BREAKDOWN, gate.getIncidents().getFirst().getType());
    }
}
