package fr.univcotedazur.skimaster.gate.connectors;

import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateStatusResponseDTO;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateSyncDTO;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.PassageDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    public List<PassageDTO> getPassagesofToday() {
        try{
            return Arrays.stream(Objects.requireNonNull(restClient.build().get().uri("/gate/passages/today").retrieve().body(PassageDTO[].class))).toList();
        } catch (Exception e) {
            System.out.println(e);
            return null;
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
