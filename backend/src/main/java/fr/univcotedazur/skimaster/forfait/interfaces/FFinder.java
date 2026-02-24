package fr.univcotedazur.skimaster.forfait.interfaces;

import fr.univcotedazur.skimaster.forfait.entities.Forfeit;
import fr.univcotedazur.skimaster.forfait.entities.PassType;

import java.util.List;

public interface FFinder {
    List<Forfeit> findForfaitByUserId(Long customerId);
    List<Forfeit> findAllForfaits();
    List<PassType> getAvailablePassTypes();
}
