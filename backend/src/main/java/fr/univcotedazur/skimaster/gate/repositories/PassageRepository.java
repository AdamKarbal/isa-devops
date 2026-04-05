package fr.univcotedazur.skimaster.gate.repositories;

import fr.univcotedazur.skimaster.gate.entities.Passage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface PassageRepository extends JpaRepository<Passage, Long> {

    @Query("SELECT p FROM Passage p WHERE p.customerId = :customerId AND p.occurrenceDate >= :startOfDay AND p.occurrenceDate < :endOfDay ORDER BY p.occurrenceDate ASC")
    List<Passage> findByCustomerIdAndDateRange(@Param("customerId") Long customerId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT p FROM Passage p WHERE p.customerId = ?1 ORDER BY p.occurrenceDate ASC")
    List<Passage> findByCustomerId(Long customerId);

    @Query("SELECT p FROM Passage p WHERE p.gatePassedBy = :gateId AND p.occurrenceDate >= :startOfDay AND p.occurrenceDate < :endOfDay ORDER BY p.occurrenceDate ASC")
    List<Passage> findByGateIdAndDateRange(
            @Param("gateId") String gateId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}


