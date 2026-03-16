package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.components.BeepStrategy;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BeepStrategyTest {

    @Autowired
    private BeepStrategy beepStrategy;

    @Mock
    private PortierInformation portierInformation;

    @Test
    void testChildBeep() {
        BeepSignal result = beepStrategy.processBeepInformation(AgeCategory.CHILD);
        assertEquals(BeepSignal.CHILD_BEEP, result);
    }

    @Test
    void testAdultBeep() {
        BeepSignal result = beepStrategy.processBeepInformation(AgeCategory.ADULT);
        assertEquals(BeepSignal.STANDARD_BEEP, result);
    }

    @Test
    void integrationTestAdultBeep() {
        AgeCategory adult = AgeCategory.ADULT;
        assertEquals(BeepSignal.STANDARD_BEEP, beepStrategy.processBeepInformation(adult));
    }

    @Test
    void integrationTestChildBeep() {
        AgeCategory child = AgeCategory.CHILD;
        assertEquals(BeepSignal.CHILD_BEEP, beepStrategy.processBeepInformation(child));
    }
}
