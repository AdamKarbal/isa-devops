package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.model.PassageRequestDTO;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@ShellComponent
public class GateSyncCommands {

    private final RestClient restClient; // Backend (8080)
    private final RestClient doorRestClient; // Door (8082)

    public GateSyncCommands(RestClient restClient, RestClient doorRestClient) {
        this.restClient = restClient;
        this.doorRestClient = doorRestClient;
    }

    @ShellMethod(value = "Push authorized badges to Gates", key = "sync-gates")
    public String syncGates() {
        restClient.post().uri("/gatesync/push").retrieve().toBodilessEntity();
        return "SUCCESS: Authorized badges pushed.";
    }

    @ShellMethod(value = "Simulate a physical passage at the door", key = "door-pass")
    public String doorPass(Long cardId, boolean accepted) {
        // On tape directement sur le service externe (autonomie)
        doorRestClient.post()
                .uri("/gate/pass")
                .body(new PassageRequestDTO(accepted, cardId))
                .retrieve()
                .toBodilessEntity();
        return "SUCCESS: Card " + cardId + " badged at the door.";
    }

    @ShellMethod(key = "collect-passages", value = "Collect passages from door and push to backend")
    public String collectPassages() {
        try {
            Long[] idsArray = doorRestClient.get()
                    .uri("/gate/passages")
                    .retrieve()
                    .body(Long[].class);
            if (idsArray == null || idsArray.length == 0) {
                return "SUCCESS: No new passages to collect at the door.";
            }
            List<Long> collectedIds = Arrays.asList(idsArray);
            restClient.post()
                    .uri("/gatesync/collect")
                    .body(collectedIds)
                    .retrieve()
                    .toBodilessEntity();

            return "SUCCESS: Collected " + collectedIds.size() + " passage(s) from door and pushed to backend: " + collectedIds;
        } catch (Exception e) {
            return "ERROR: Communication failed. Check if Door and Backend are up. Details: " + e.getMessage();
        }
    }
}
