package fr.univcotedazur.skimaster.gate.connectors;

import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateStatusResponseDTO;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateSyncDTO;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.PassageDTO;
import fr.univcotedazur.skimaster.gate.exceptions.NoGateExistentException;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.*;

@Component
public class TurnstileProxy {

    private final RestClient.Builder restClient;
    private final Map<String, String> gateUrls;

    public TurnstileProxy(RestClient.Builder builder, Environment environment) {
        this.restClient = builder;
        this.gateUrls = Binder.get(environment)
                .bind("gate.instances", Bindable.mapOf(String.class, String.class))
                .orElseGet(Map::of);
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

    public void syncAllGates(List<Long> cardIds){
        gateUrls.values().forEach(url -> syncAuthorizedCards(url, cardIds));
    }

    public List<PassageDTO> getPassagesofTodayForGate(String code) {
        String targetUrl = getUrl(code);

        try{
            return Arrays.stream(Objects.requireNonNull(restClient.baseUrl(targetUrl).build().get().uri("/gate/passages/today").retrieve().body(PassageDTO[].class))).toList();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public List<PassageDTO> getPassagesofToday(){
        List<PassageDTO> passages = new ArrayList<>();

        gateUrls.values().forEach(url -> passages.addAll(getPassagesofTodayForGate(url)));
        return passages;
    }

    public long syncOpeningTimeByGate(String code) {
        String targetUrl = getUrl(code);
        try{
            Long result = restClient.baseUrl(targetUrl)
                    .build()
                    .get()
                    .uri("/gate/opening-time")
                    .retrieve()
                    .body(Long.class);

            if (result == null) {
                System.err.println("Avertissement : Le corps de la réponse pour 'opening-time' est vide pour l'URL : " + targetUrl);
                return -1L;
            }

            return result;

        }
        catch (Exception e) {
            System.err.println("Erreur lors de la récupération du temps d'ouverture pour " + targetUrl + ": " + e.getMessage());
            return -1L;
        }
    }

    public void sendOrder(String code, String order){
        String targetUrl = getUrl(code);
        restClient.baseUrl(targetUrl).build()
                .post()
                .uri("/gate" + order)
                .retrieve()
                .toBodilessEntity();
    }

    public GateStatusResponseDTO getGateStatus(String code){
        String targetUrl = getUrl(code);

        return restClient.baseUrl(targetUrl).build()
                .get()
                .uri("/gate/status")
                .retrieve()
                .body(GateStatusResponseDTO.class);
    }

    public void sendOrderAll(String order){
        gateUrls.values().forEach(url -> sendOrder(url, order));
    }

    private String getUrl(String code){
        String url = gateUrls.get(code);
        if (url == null) throw new NoGateExistentException("Code de porte inconnu : " + code);
        return url;
    }

}
