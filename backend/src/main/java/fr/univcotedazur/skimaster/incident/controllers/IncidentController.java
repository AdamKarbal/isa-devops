package fr.univcotedazur.skimaster.incident.controllers;

import fr.univcotedazur.skimaster.common.dto.ErrorDTO;
import fr.univcotedazur.skimaster.forfait.components.ForfaitRegistry;
import fr.univcotedazur.skimaster.forfait.dto.ForfaitDTO;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.interfaces.FFinder;
import fr.univcotedazur.skimaster.incident.components.IncidentService;
import fr.univcotedazur.skimaster.incident.dto.IncidentDTO;
import fr.univcotedazur.skimaster.incident.entities.Incident;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = IncidentController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class IncidentController {
    public static final String BASE_URI = "/incidents";

    private final IncidentService service;

    public IncidentController(IncidentService service) {
        this.service = service;
    }

    @GetMapping("/getAllIncidents")
    public ResponseEntity<List<IncidentDTO>> getAllIncidents(){
        List<Incident> incidents = service.findAllIncidents();
        List<IncidentDTO> dtos = incidents.stream()
                .map(fr.univcotedazur.skimaster.incident.controllers.IncidentController::incidentToDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/addIncident")
    public ResponseEntity<Void> addIncident(@RequestBody IncidentDTO dto){
        Incident incident = new Incident(dto.type(), dto.description(), dto.time());
        service.saveIncident(incident);
        return ResponseEntity.noContent().build();
    }

    private static IncidentDTO incidentToDTO(Incident i){
        return new IncidentDTO(
                i.getId(),
                i.getType(),
                i.getDescription(),
                i.getTime()
        );
    }

}
