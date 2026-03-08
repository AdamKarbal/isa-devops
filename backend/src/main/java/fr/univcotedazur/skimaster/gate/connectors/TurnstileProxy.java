package fr.univcotedazur.skimaster.gate.connectors;

import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateStatusResponseDTO;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateSyncDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;

@Component
public class TurnstileProxy {

    private final RestClient.Builder restClient;

    public TurnstileProxy(RestClient.Builder builder) {
        this.restClient = builder;
    }

    public void syncAuthorizedCards(String targetUrl, List<Long> cardIds) {
        try {
            restClient.baseUrl(targetUrl).build()
                    .post()
                    .uri("/gate/sync")
                    .body(new GateSyncDTO(cardIds))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            System.err.println("Échec de synchronisation pour la porte : " + targetUrl);
        }
    }

    public void sendOrder(String baseUrl, String order){
        restClient.baseUrl(baseUrl).build()
                .post()
                .uri("/gate" + order)
                .retrieve()
                .toBodilessEntity();
    }

    public GateStatusResponseDTO getGateStatus(String baseUrl){
        return restClient.baseUrl(baseUrl).build()
                .get()
                .uri("/gate/status")
                .retrieve()
                .body(GateStatusResponseDTO.class);
    }

}
