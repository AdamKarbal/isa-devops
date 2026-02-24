package fr.univcotedazur.skimaster.gate.repositories;

import fr.univcotedazur.skimaster.gate.entities.Passage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassageRepository extends JpaRepository<Passage, Long> {
    Optional<Passage> findLastPassageByCustomerId(Long customerId);
}
