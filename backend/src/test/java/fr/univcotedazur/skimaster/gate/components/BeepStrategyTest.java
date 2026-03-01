package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@SpringBootTest
class BeepStrategyTest {
    @Autowired
    private BeepStrategy beepStrategy;

    @MockitoBean
    private PortierInformation portierInformation;

    @Test
    void integrationTestChildBeep() {
        BeepContext context = new BeepContext(1L, 2L, LocalDateTime.now());
        beepStrategy.assignAuthorizationBeep(context, AgeCategory.CHILD);
        verify(portierInformation).authorizePassage(2L, BeepSignal.CHILD_BEEP);
    }

    @Test
    void integrationTestAdultBeep() {
        BeepContext context = new BeepContext(5L, 2L, LocalDateTime.now());
        beepStrategy.assignAuthorizationBeep(context, AgeCategory.ADULT);
        verify(portierInformation).authorizePassage(2L, BeepSignal.STANDARD_BEEP);
    }
}
