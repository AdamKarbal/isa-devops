package fr.univcotedazur.skimaster.gate.interfaces;

import fr.univcotedazur.skimaster.gate.dtos.AccessRequest;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;

public interface PortierInformation {

    AccessRequest authorizePassage(Long forfeitId, BeepSignal signal);
    AccessRequest denyPassage();


}
