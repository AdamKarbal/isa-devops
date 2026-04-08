package fr.univcotedazur.skimaster.incident.controllers;

import fr.univcotedazur.skimaster.incident.components.IncidentService;
import fr.univcotedazur.skimaster.incident.dto.IncidentDTO;
import fr.univcotedazur.skimaster.incident.entities.Incident;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/getLast")
    public ResponseEntity<IncidentDTO> getLastIncident(){
        return service.findFirstByOrderByTimeDesc()
                .map(IncidentController::incidentToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(null));
    }

    @PostMapping("/addIncident")
    public ResponseEntity<Void> addIncident(@RequestBody IncidentDTO dto){
        service.reportIncident(dto);
        return ResponseEntity.noContent().build();
    }

    private static IncidentDTO incidentToDTO(Incident i){
        return new IncidentDTO(
                i.getId(),
                i.getType(),
                i.getDescription(),
                i.getTime(),
                i.getTargetType(),
                i.getTargetCode(),
                i.isClosing()
        );
    }

}
