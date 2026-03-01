package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.gate.dtos.AccessRequest;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.interfaces.BeepInformation;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
import org.springframework.stereotype.Service;

@Service
public class BeepStrategy implements BeepInformation {

    private final PortierInformation portierInformation;

    public BeepStrategy(PortierInformation portierInformation) {
        this.portierInformation = portierInformation;
    }

    @Override
    public AccessRequest assignAuthorizationBeep(BeepContext beep, AgeCategory age) {
        BeepSignal signalSend = age.equals(AgeCategory.CHILD)?BeepSignal.CHILD_BEEP:BeepSignal.STANDARD_BEEP;
        return portierInformation.authorizePassage(beep.cardId(), signalSend);
    }

    @Override
    public AccessRequest assignForbiddenBeep(BeepContext beep) {
        return portierInformation.denyPassage();
    }
}
