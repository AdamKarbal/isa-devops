package fr.univcotedazur.skimaster.incident.components;

import fr.univcotedazur.skimaster.incident.entities.Incident;
import fr.univcotedazur.skimaster.incident.entities.IncidentType;
import fr.univcotedazur.skimaster.incident.repositories.IncidentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    @Autowired
    private final IncidentRepository repository;

    public IncidentService(IncidentRepository repository) {
        this.repository = repository;
    }

    public Optional<Incident> findFirstByOrderByTimeDesc() {
        return repository.findFirstByOrderByTimeDesc();
    }

    @Transactional
    public void saveIncident(Incident incident){
        repository.save(incident);
    }

    @Transactional
    public List<Incident> findAllIncidents(){
        return repository.findAll();
    }

    @Transactional
    public List<Incident> findIncidentByType(IncidentType type){
        return repository.findByType(type);
    }
}
