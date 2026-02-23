package fr.univcotedazur.skimaster.forfait.interfaces;

import fr.univcotedazur.skimaster.forfait.entities.Forfait;

import java.util.List;

public interface FRegister {
    void registerForfait(Forfait forfait, Long CustomerId);
    void registerForfaits(List<Forfait> forfaits, Long CustomerId);
}
