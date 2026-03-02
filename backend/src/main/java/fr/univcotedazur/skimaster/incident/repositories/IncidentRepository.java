package fr.univcotedazur.skimaster.incident.repositories;

import fr.univcotedazur.skimaster.gate.entities.Gate;
import fr.univcotedazur.skimaster.incident.entities.Incident;
import fr.univcotedazur.skimaster.incident.entities.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByType(IncidentType type);
}
