package fr.univcotedazur.skimaster.incident.repositories;
import fr.univcotedazur.skimaster.incident.entities.Incident;
import fr.univcotedazur.skimaster.incident.entities.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByType(IncidentType type);
    Optional<Incident> findFirstByOrderByTimeDesc();
    List<Incident> findByResolvedFalse();
    List<Incident> findByTargetCodeAndResolvedFalse(String targetCode);
    List<Incident> findByTargetCodeAndResolvedFalseAndClosesTrue(String targetCode);
    List<Incident> findByResolvedFalseAndClosesTrue();
}
