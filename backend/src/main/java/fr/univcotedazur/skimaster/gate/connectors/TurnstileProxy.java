package fr.univcotedazur.skimaster.gate.connectors;

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

    private final RestClient restClient;

    public TurnstileProxy(@Value("${gate.host.baseurl}") String gateHost, RestClient.Builder builder) {
        this.restClient = builder.baseUrl(gateHost).build();
    }

    public void syncAuthorizedCards(List<Long> authorizedIds) {
        GateSyncDTO dto = new GateSyncDTO(authorizedIds);
        restClient.post()
                .uri("/gate/sync")
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }
    public List<PassageDTO> getPassagesofToday() {
        try{
            return Arrays.stream(Objects.requireNonNull(restClient.get().uri("/gate/passages/today").retrieve().body(PassageDTO[].class))).toList();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
