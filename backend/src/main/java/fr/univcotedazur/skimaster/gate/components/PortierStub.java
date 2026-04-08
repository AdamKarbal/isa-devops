package fr.univcotedazur.skimaster.gate.components;


import fr.univcotedazur.skimaster.gate.dtos.AccessRequest;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
import org.springframework.stereotype.Component;

//CLASSE BOUCHE-TROU A SUPPRIMER ABSOLUMENT PLUS TARD!!!
@Component
public class PortierStub implements PortierInformation {

    @Override
    public AccessRequest authorizePassage(String code, Long forfeitId, BeepSignal signal){
        return new AccessRequest(forfeitId, signal, true);
    }

    @Override
    public AccessRequest denyPassage(String code){
        return new AccessRequest(null, null, false);
    }
}
