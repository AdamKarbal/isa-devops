package fr.univcotedazur.skimaster.gate.components;

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
    public void assignBeep(BeepContext beep,int age) {
        BeepSignal signalSend = age<=12?BeepSignal.CHILD_BEEP:BeepSignal.STANDARD_BEEP;
        portierInformation.authorizePassage(beep.gateId(), signalSend);
    }
}
