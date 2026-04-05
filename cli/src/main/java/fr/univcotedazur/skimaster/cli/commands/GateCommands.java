package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import fr.univcotedazur.skimaster.cli.model.GateAlertThresholdRequestDTO;
import fr.univcotedazur.skimaster.cli.model.GateMonitoringStatusDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;

import java.util.List;

@ShellComponent
public class GateCommands {
    public static final String GATE_BASE_URI = "/gate";
    public static final String GATES_BASE_URI = "/gates";

    private final RestClient restClient;

    public GateCommands(RestClient restClient, CliContext cliContext) {
        this.restClient = restClient;
    }

    @ShellMethod(value = "Open a gate", key = "open-gate")
    public String openGate() {
        restClient.post()
                .uri(GATE_BASE_URI + "/open")
                .retrieve()
                .toBodilessEntity();

        return "Le portique a ete ouvert avec succes.";
    }

    @ShellMethod(value = "Close a gate", key = "close-gate")
    public String closeGate() {
        restClient.post()
                .uri(GATE_BASE_URI + "/close")
                .retrieve()
                .toBodilessEntity();

        return "Le portique a ete ferme avec succes.";
    }

    @ShellMethod(value = "Configurer la jauge d'alerte d'un portique (set-gate-threshold GATE_CODE THRESHOLD)", key = "set-gate-threshold")
    public String setGateThreshold(String gateCode, int threshold) {
        restClient.put()
                .uri(GATES_BASE_URI + "/{gateCode}/alert-threshold", gateCode)
                .body(new GateAlertThresholdRequestDTO(threshold))
                .retrieve()
                .toBodilessEntity();
        return "Jauge d'alerte du portique " + gateCode + " mise a " + threshold + ".";
    }

    @ShellMethod(value = "Afficher les statuts consolides des portiques", key = "gates-status")
    public String gatesStatus() {
        List<GateMonitoringStatusDTO> statuses = restClient.get()
                .uri(GATES_BASE_URI + "/status")
                .retrieve()
                .body(new ParameterizedTypeReference<List<GateMonitoringStatusDTO>>() {
                });

        if (statuses == null || statuses.isEmpty()) {
            return "Aucun portique configure.";
        }

        StringBuilder sb = new StringBuilder("--- STATUTS PORTIQUES ---\n");
        for (GateMonitoringStatusDTO status : statuses) {
            sb.append("Portique=").append(status.gateId())
                    .append(" | Etat=").append(status.status())
                    .append(" | JaugeAlerte=").append(status.alertThreshold())
                    .append('\n');
        }
        return sb.toString();
    }
}
