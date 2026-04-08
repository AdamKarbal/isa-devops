package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BeepStrategyTest {

    private BeepStrategy beepStrategy;

    @Mock
    private PortierInformation portierInformation;

    @BeforeEach
    void setUp() {
        beepStrategy = new BeepStrategy();
    }

    @Test
    void testChildBeep() {
        BeepSignal result = beepStrategy.processBeepInformation(AgeCategory.CHILD);
        assertEquals(BeepSignal.CHILD_BEEP, result);
    }

    @Test
    void testAdultBeep() {
        BeepSignal resultReal = beepStrategy.processBeepInformation(AgeCategory.ADULT);
        assertEquals(BeepSignal.STANDARD_BEEP, resultReal);
    }

    @Test
    void integrationTestAdultBeep() {
        assertEquals(BeepSignal.STANDARD_BEEP, beepStrategy.processBeepInformation(AgeCategory.ADULT));
    }

    @Test
    void integrationTestChildBeep() {
        assertEquals(BeepSignal.CHILD_BEEP, beepStrategy.processBeepInformation(AgeCategory.CHILD));
    }
}
