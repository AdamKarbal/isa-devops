package fr.univcotedazur.skimaster.gate.interfaces;

import fr.univcotedazur.skimaster.customer.dto.CustomerDTO;
import fr.univcotedazur.skimaster.gate.entities.Passage;

import java.util.List;

public interface AccessDealer {

    List<Passage> fetchRecentPassages();
    void sendWhiteListToGates();
}
