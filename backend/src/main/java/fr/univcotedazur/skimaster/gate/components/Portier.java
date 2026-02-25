package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.gate.dtos.AccessRequest;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
import org.springframework.stereotype.Component;


@Component
public class Portier implements PortierInformation{

public Portier() {

}
    @Override
    public AccessRequest authorizePassage(Long gateId, Long forfeitId,BeepSignal signal) {
    //check gate Opened
        return new AccessRequest(forfeitId,signal,true);
    }

    @Override
    public AccessRequest denyPassage(Long gateId,Long forfeitId) {
    //same
        return new AccessRequest(forfeitId,BeepSignal.ACCESS_DENIED,false);
    }
}
