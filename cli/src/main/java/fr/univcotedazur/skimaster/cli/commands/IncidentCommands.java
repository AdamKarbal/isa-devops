package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.model.IncidentDTO;
import fr.univcotedazur.skimaster.cli.model.IncidentTargetType;
import fr.univcotedazur.skimaster.cli.model.IncidentType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

@ShellComponent
public class IncidentCommands {
    private static final String BASE_URI = "/incidents";

    private final RestClient restClient;

    public IncidentCommands(RestClient restClient) {
        this.restClient = restClient;
    }

    @ShellMethod(value = "Signaler un incident (ex: report-problem EQUIPMENT_FAILURE PORTIQUE G-NORD-01 \"Panne de portique\" true)", key = "report-problem")
    public String report(IncidentType type,
                         IncidentTargetType targetType,
                         String targetCode,
                         String description,
                         @ShellOption(defaultValue = "false") boolean closes) {
        IncidentDTO dto = new IncidentDTO(null, type, description, LocalDateTime.now(), targetType, targetCode, closes);

        restClient.post()
                .uri(BASE_URI + "/addIncident")
                .body(dto)
                .retrieve()
                .toBodilessEntity();

        return "Incident envoye au serveur avec succes.";
    }

    @ShellMethod(value = "Voir tous les incidents", key = "incidents-history")
    public String history() {
        List<IncidentDTO> incidents = restClient.get()
                .uri(BASE_URI + "/getAllIncidents")
                .retrieve()
                .body(new ParameterizedTypeReference<List<IncidentDTO>>() {});

        if (incidents == null || incidents.isEmpty()) {
            return "Aucun incident signale pour le moment.";
        }

        StringBuilder sb = new StringBuilder("--- HISTORIQUE DES INCIDENTS ---\n");
        for (IncidentDTO incident : incidents) {
            sb.append(String.format("[%s] %-10s [%s %s] : %s (ID: %d, closes=%s)\n",
                    incident.time(),
                    incident.type(),
                    incident.targetType(),
                    incident.targetCode(),
                    incident.description(),
                    incident.id(),
                    incident.closes()));
        }
        return sb.toString();
    }

    @ShellMethod(value = "Voir le dernier incident signale", key = "last-incident")
    public String lastIncident() {
        IncidentDTO dto = restClient.get()
                .uri(BASE_URI + "/getLast")
                .retrieve()
                .body(IncidentDTO.class);

        if (dto == null) {
            return "Aucun incident signale pour le moment.";
        }

        return String.format("--- DERNIER INCIDENT ---%n[%s] %-10s [%s %s] : %s (ID: %d, closes=%s)%n",
                dto.time(),
                dto.type(),
                dto.targetType(),
                dto.targetCode(),
                dto.description(),
                dto.id(),
                dto.closes());
    }
}
