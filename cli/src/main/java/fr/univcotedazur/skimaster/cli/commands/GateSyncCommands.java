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
    public String doorPass(Long cardId) {
        try {
            doorRestClient.post()
                    .uri("/gate/pass")
                    .body(new PassageRequestDTO(true, cardId))
                    .retrieve()
                    .toBodilessEntity();

            return "SUCCESS: Card " + cardId + " badged at the door.";
        }
        catch (ErrorResponseException e) {
            return "DENIED: " + e.getDetailMessageCode();
        }
        catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @ShellMethod(key = "collect-passages", value = "Collect passages from door and push to backend")
    public String collectPassages() {
        try {
            PassageDTO[] passagesArray = doorRestClient.get()
                    .uri("/gate/passages")
                    .retrieve()
                    .body(PassageDTO[].class);

            if (passagesArray == null || passagesArray.length == 0) {
                return "SUCCESS: No new passages to collect at the door.";
            }
            List<PassageDTO> collectedPassages = Arrays.asList(passagesArray);
            List<Long> ids = collectedPassages.stream().map(PassageDTO::nfcCardId).toList();
            restClient.post()
                    .uri("/gatesync/collect")
                    .body(ids)
                    .retrieve()
                    .toBodilessEntity();
            return "SUCCESS: Collected " + ids.size() + " passage(s) and pushed to backend.";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

}
