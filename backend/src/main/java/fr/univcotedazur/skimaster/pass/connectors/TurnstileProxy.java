package fr.univcotedazur.skimaster.pass.connectors;

import fr.univcotedazur.skimaster.pass.connectors.externaldto.GateSyncDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;

@Component
public class TurnstileProxy {

    private final RestClient restClient;

    public TurnstileProxy(@Value("${gate.host.baseurl}") String gateHost, RestClient.Builder builder) {
        this.restClient = builder.baseUrl(gateHost).build();
    }

    public void syncAuthorizedCards(List<Long> cardIds) {
        try {
            restClient.post()
                    .uri("/gate/sync")
                    .body(new GateSyncDTO(cardIds))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
        }
    }
}
