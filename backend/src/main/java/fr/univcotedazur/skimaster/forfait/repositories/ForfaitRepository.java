package fr.univcotedazur.skimaster.forfait.repositories;

import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForfaitRepository extends JpaRepository<Forfait, Long> {
    List<Forfait> findByUserId(Long customerId);
    Optional<Forfait> findFirstByCustomerId(Long customerId);
    List<Forfait> findAllByCustomerId(Long customerId);
}
