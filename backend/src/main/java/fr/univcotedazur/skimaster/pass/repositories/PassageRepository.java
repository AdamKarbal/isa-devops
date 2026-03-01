package fr.univcotedazur.skimaster.pass.repositories;

import fr.univcotedazur.skimaster.pass.entities.Passage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface PassageRepository extends JpaRepository<Passage, Long> {

    @Query("SELECT p FROM Passage p WHERE p.customerId = ?1 AND CAST(p.occurrenceDate AS date) = ?2 ORDER BY p.occurrenceDate ASC")
    List<Passage> findByCustomerIdAndDate(Long customerId, LocalDate date);
}
