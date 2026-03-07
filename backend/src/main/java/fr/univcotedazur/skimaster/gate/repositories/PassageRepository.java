package fr.univcotedazur.skimaster.gate.repositories;

import fr.univcotedazur.skimaster.gate.entities.Passage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface PassageRepository extends JpaRepository<Passage, Long> {

    @Query("SELECT p FROM Passage p WHERE p.customerId = ?1 AND CAST(p.occurrenceDate AS date) = ?2 ORDER BY p.occurrenceDate ASC")
    List<Passage> findByCustomerIdAndDate(Long customerId, LocalDate date);

    @Query("SELECT p FROM Passage p WHERE p.customerId = ?1 ORDER BY p.occurrenceDate ASC")
    List<Passage> findByCustomerId(Long customerId);

    @Query("SELECT p FROM Passage p WHERE CAST(p.occurrenceDate AS date) = ?1 ORDER BY p.occurrenceDate ASC")
    List<Passage> findByDate(LocalDate localDate);
}
