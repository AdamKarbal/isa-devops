package fr.univcotedazur.skimaster.gate.interfaces;

import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;

public interface AccessDealer {

    void processCurrentPassage(BeepContext beepContext) throws CustomerIdNotFoundException;
    void computeWhiteList();
}
