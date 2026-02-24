package fr.univcotedazur.skimaster.gate.repositories;

import fr.univcotedazur.skimaster.gate.entities.Forfeit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForfeitRepository {
    List<Forfeit> getAllForfaits();
}
