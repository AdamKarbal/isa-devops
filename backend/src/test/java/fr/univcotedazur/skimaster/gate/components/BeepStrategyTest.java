package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BeepStrategyTest {
    @InjectMocks
    private BeepStrategy beepStrategy;

    @Mock
    private PortierInformation portierInformation;

    @Test
    void integrationTestChildBeep() {
        BeepContext context = new BeepContext(1L, 2L, LocalDateTime.now());
        beepStrategy.assignAuthorizationBeep(context, AgeCategory.CHILD);
        verify(portierInformation).authorizePassage(context.cardId(), BeepSignal.CHILD_BEEP);
    }

    @Test
    void integrationTestAdultBeep() {
        BeepContext context = new BeepContext(5L, 2L, LocalDateTime.now());
        beepStrategy.assignAuthorizationBeep(context, AgeCategory.ADULT);
        verify(portierInformation).authorizePassage(context.cardId(), BeepSignal.STANDARD_BEEP);
    }
}
