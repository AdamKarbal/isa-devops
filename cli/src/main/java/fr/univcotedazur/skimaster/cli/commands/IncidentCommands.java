package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.incident.dto.IncidentDTO;
import fr.univcotedazur.skimaster.incident.entities.Incident;
import fr.univcotedazur.skimaster.incident.entities.IncidentType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;
import fr.univcotedazur.skimaster.incident.components.IncidentService;

import java.time.LocalDateTime;
import java.util.List;

@ShellComponent
public class IncidentCommands {
    private static final String BASE_URI = "/incidents";

    private final RestClient restClient;

    public IncidentCommands(RestClient restClient){
        this.restClient = restClient;
    }

    @ShellMethod(value = "Signaler un incident (ex: report PANNE 'Panne de portique')", key = "report-problem")
    public String report(IncidentType type, String description){
        IncidentDTO dto = new IncidentDTO(null, type, description, LocalDateTime.now());

        restClient.post()
                .uri(BASE_URI + "/addIncident")
                .body(dto)
                .retrieve()
                .toBodilessEntity();

        return "Incident envoyé au serveur avec succès.";
    }

    @ShellMethod(value = "Voir tous les incidents signalés", key = "incidents-history")
    public List<IncidentDTO> history(){
        return restClient.get()
                .uri(BASE_URI + "/getAllIncidents")
                .retrieve()
                .body(new ParameterizedTypeReference<List<IncidentDTO>>() {});
    }

}
