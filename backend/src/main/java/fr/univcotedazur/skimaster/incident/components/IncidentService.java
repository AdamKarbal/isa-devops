package fr.univcotedazur.skimaster.incident.components;

import fr.univcotedazur.skimaster.incident.entities.Incident;
import fr.univcotedazur.skimaster.incident.entities.IncidentType;
import fr.univcotedazur.skimaster.incident.repositories.IncidentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService{
    private final IncidentRepository repository;

    public IncidentService(IncidentRepository repository) {
        this.repository = repository;
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
