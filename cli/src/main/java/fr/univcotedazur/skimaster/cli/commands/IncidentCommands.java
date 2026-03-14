package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.model.IncidentTargetType;
import fr.univcotedazur.skimaster.cli.model.IncidentType;
import fr.univcotedazur.skimaster.cli.model.IncidentDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

@ShellComponent
public class IncidentCommands {
    private static final String BASE_URI = "/incidents";

    private final RestClient restClient;

    public IncidentCommands(RestClient restClient){
        this.restClient = restClient;
    }

    @ShellMethod(value = "Signaler un incident (ex: report-problem EQUIPMENT_FAILURE PORTIQUE G-NORD-01 \"Panne de portique\")", key = "report-problem")
    public String report(IncidentType type, IncidentTargetType targetType, String targetCode, String description){
        IncidentDTO dto = new IncidentDTO(null, type, description, LocalDateTime.now(), targetType, targetCode);

        restClient.post()
                .uri(BASE_URI + "/addIncident")
                .body(dto)
                .retrieve()
                .toBodilessEntity();

        return "Incident envoyé au serveur avec succès.";
    }

    @ShellMethod(value = "Voir tous les incidents", key = "incidents-history")
    public String history() {
        List<IncidentDTO> incidents = restClient.get()
                .uri(BASE_URI + "/getAllIncidents")
                .retrieve()
                .body(new ParameterizedTypeReference<List<IncidentDTO>>() {});

        if (incidents == null || incidents.isEmpty()) {
            return "Aucun incident signalé pour le moment.";
        }

        StringBuilder sb = new StringBuilder("--- HISTORIQUE DES INCIDENTS ---\n");
        for (IncidentDTO i : incidents) {
            sb.append(String.format("[%s] %-10s [%s %s] : %s (ID: %d)\n",
                    i.time(), i.type(), i.targetType(), i.targetCode(), i.description(), i.id()));
        }
        return sb.toString();
    }

    @ShellMethod(value = "Voir le dernier incident signalé", key = "last-incident")
    public String lastIncident(){
        IncidentDTO dto = restClient.get()
                .uri(BASE_URI + "/getLast")
                .retrieve()
                .body(IncidentDTO.class);

        if(dto == null){
            return "Aucun incident signalé pour le moment.";
        }

        StringBuilder sb = new StringBuilder("--- DERNIER INCIDENT ---\n");
        sb.append(String.format("[%s] %-10s [%s %s] : %s (ID: %d)\n",
                dto.time(), dto.type(), dto.targetType(), dto.targetCode(), dto.description(), dto.id()));
        return sb.toString();
    }

}
