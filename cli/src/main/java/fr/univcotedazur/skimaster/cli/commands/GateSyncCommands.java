package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.model.PassageDTO;
import fr.univcotedazur.skimaster.cli.model.PassageRequestDTO;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@ShellComponent
public class GateSyncCommands {

    private final RestClient restClient; // Backend (8080)
    private final RestClient doorRestClient; // Door (8082)
    private final RestClient.Builder restClientBuilder;

    public GateSyncCommands(RestClient restClient, RestClient doorRestClient, RestClient.Builder restClientBuilder) {
        this.restClient = restClient;
        this.doorRestClient = doorRestClient;
        this.restClientBuilder = restClientBuilder;
    }

    @ShellMethod(value = "Push authorized badges to Gates", key = "sync-gates")
    public String syncGates() {
        restClient.post().uri("/gatesync/push").retrieve().toBodilessEntity();
        return "SUCCESS: Authorized badges pushed.";
    }

    @ShellMethod(value = "Simulate passage at a specific gate", key = "door-pass")
    public String doorPass(Long cardId, String gateServiceName) {
        String targetUrl = "http://" + gateServiceName + ":8082";

        try {
            restClientBuilder.baseUrl(targetUrl).build()
                    .post()
                    .uri("/gate/pass")
                    .body(new PassageRequestDTO(true, cardId))
                    .retrieve()
                    .toBodilessEntity();

            return "SUCCESS: Card " + cardId + " badged at " + gateServiceName + " (" + targetUrl + ")";
        } catch (Exception e) {
            return "ERROR: Could not reach " + gateServiceName + ". " + e.getMessage();
        }
    }

    @ShellMethod(key = "collect-passages", value = "Trigger backend to collect passages")
    public String collectPassages() {
        try {
            Long[] idsArray = restClient.post()
                    .uri("/gatesync/collect")
                    .retrieve()
                    .body(Long[].class);
            if (idsArray == null || idsArray.length == 0) {
                return "COLLECT: No new passages found on any gate.";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("COLLECTED PASSAGES FROM GATES: \n");
            for (Long id : idsArray) {
                sb.append(" - Customer ID: ").append(id).append("\n");
            }
            sb.append("Total: ").append(idsArray.length).append(" passage(s) sent to billing logic.");

            return sb.toString();
        } catch (Exception e) {
            return "ERROR during collection: " + e.getMessage();
        }
    }

}
