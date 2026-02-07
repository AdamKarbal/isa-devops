package fr.univcotedazur.skimaster.forfait.interfaces;

import fr.univcotedazur.skimaster.forfait.entities.Forfait;

import java.util.List;

public interface FFinder {
    List<Forfait> getForfait(Long customerId);
    List<Forfait> getAllForfaits();
}
