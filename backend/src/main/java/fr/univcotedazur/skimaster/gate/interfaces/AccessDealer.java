package fr.univcotedazur.skimaster.gate.interfaces;

import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.gate.dtos.AccessRequest;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;

import java.util.List;

public interface AccessDealer {
    public void synchronizeExternalSystems();
    public List<Long> handleIncomingData();
}
