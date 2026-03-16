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

    public void syncAuthorizedCards(String baseUrl, List<Long> authorizedIds) {
        GateSyncDTO dto = new GateSyncDTO(authorizedIds);
        restClientBuilder.baseUrl(baseUrl).build()
                .post()
                .uri("/gate/sync")
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    public List<PassageDTO> getPassagesofToday(String baseUrl) {
        try {
            PassageDTO[] response = restClientBuilder.baseUrl(baseUrl).build()
                    .get()
                    .uri("/gate/passages/today")
                    .retrieve()
                    .body(PassageDTO[].class);
            return (response != null) ? Arrays.asList(response) : List.of();
        } catch (Exception e) {
            return List.of();
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
}
