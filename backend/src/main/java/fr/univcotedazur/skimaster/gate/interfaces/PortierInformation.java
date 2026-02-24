package fr.univcotedazur.skimaster.gate.interfaces;

import fr.univcotedazur.skimaster.gate.enums.BeepSignal;

public interface PortierInformation {

    void authorizePassage(Long gateId,BeepSignal signal);
    void denyPassage(Long gateId, String reason);

    void closeGateById(Long gateId);


}
