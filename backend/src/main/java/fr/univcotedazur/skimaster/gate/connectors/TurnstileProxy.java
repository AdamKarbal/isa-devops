package fr.univcotedazur.skimaster.gate.connectors;

import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateStatusResponseDTO;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateSyncDTO;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.PassageDTO;
import fr.univcotedazur.skimaster.gate.entities.Passage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class TurnstileProxy {

    private final RestClient.Builder restClientBuilder;

    public TurnstileProxy(RestClient.Builder builder) {
        this.restClientBuilder = builder;
    }


    public List<PassageDTO> getPassagesofToday(String baseUrl) {
        try {
            PassageDTO[] response = restClientBuilder.baseUrl(baseUrl).build()
                    .get()
                    .uri("/gate/passages/today")
                    .retrieve()
                    .body(PassageDTO[].class);
            return (response != null) ? Arrays.asList(response) : List.of();
            }catch (Exception e){
            return List.of();
        }
        }
    public void syncAuthorizedCards(String targetUrl, List<Long> cardIds) {
        try {
            restClientBuilder.baseUrl(targetUrl).build()
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
            return Arrays.stream(Objects.requireNonNull(restClientBuilder.build().get().uri("/gate/passages/today").retrieve().body(PassageDTO[].class))).toList();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


    public void sendOrder(String baseUrl, String order) {
        restClientBuilder.baseUrl(baseUrl).build()
                .post()
                .uri("/gate" + order)
                .retrieve()
                .toBodilessEntity();
    }

    public GateStatusResponseDTO getGateStatus(String baseUrl) {
        return restClientBuilder.baseUrl(baseUrl).build()
                .get()
                .uri("/gate/status")
                .retrieve()
                .body(GateStatusResponseDTO.class);
    }

    public long syncOpeningTimeByGate(String gateId) {
        try{
            return restClientBuilder.build().get().uri("/gate/opening-time/{gateId}", gateId).retrieve().body(Long.class);
        }catch (Exception e) {
            System.out.println(e);
            return -1;
        }
    }



}
