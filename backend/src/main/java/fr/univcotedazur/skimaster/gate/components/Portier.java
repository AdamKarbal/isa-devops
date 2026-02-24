package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
import org.springframework.stereotype.Component;


@Component
public class Portier implements PortierInformation{

public Portier() {

}
    @Override
    public void authorizePassage(Long gateId, BeepSignal signal) {
        // TODO
    }

    @Override
    public void denyPassage(Long gateId, String reason) {
        // TODO
    }

    @Override
    public void closeGateById(Long gateId) {
        //TODO
    }
}
