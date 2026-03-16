package fr.univcotedazur.skimaster.gate.repositories;

import fr.univcotedazur.skimaster.gate.entities.Passage;

import java.util.List;
import java.util.Optional;

public interface PassageFinder {
    List<Passage> findAllPassagesByCustomer(Long customerId);
}
