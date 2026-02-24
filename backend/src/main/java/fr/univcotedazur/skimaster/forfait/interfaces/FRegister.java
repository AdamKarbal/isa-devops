package fr.univcotedazur.skimaster.forfait.interfaces;

import fr.univcotedazur.skimaster.forfait.entities.Forfeit;

import java.util.List;

public interface FRegister {
    void registerForfait(Forfeit forfeit, Long CustomerId);
    void registerForfaits(List<Forfeit> forfeits, Long CustomerId);
}
