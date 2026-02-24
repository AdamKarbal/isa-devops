package fr.univcotedazur.skimaster.forfait.repositories;

import fr.univcotedazur.skimaster.forfait.entities.Forfeit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForfeitRepository extends JpaRepository<Forfeit, Long> {
    List<Forfeit> findByUserId(Long customerId);
}
