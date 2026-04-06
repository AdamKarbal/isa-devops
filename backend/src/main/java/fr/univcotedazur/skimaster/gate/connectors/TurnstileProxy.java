package fr.univcotedazur.skimaster.gate.connectors;

import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateAlertThresholdUpdateDTO;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateStatusResponseDTO;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateSyncDTO;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.PassageDTO;
import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.gate.exceptions.NoGateExistentException;
import fr.univcotedazur.skimaster.gate.repositories.PassageRepository;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class TurnstileProxy {

    private final RestClient.Builder restClientBuilder;
    private final Map<String, String> gateUrls;
    private final PassageRepository passageRepository;

    public TurnstileProxy(RestClient.Builder builder, PassageRepository passageRepository, Environment environment) {
        this.restClientBuilder = builder;
        this.passageRepository = passageRepository;
        this.gateUrls = Binder.get(environment)
                .bind("gate.instances", Bindable.mapOf(String.class, String.class))
                .orElseGet(Map::of);
    }


    public List<PassageDTO> getPassagesofToday(String baseUrl) {
        try {
            PassageDTO[] response = restClientBuilder.baseUrl(baseUrl).build()
                    .get()
                    .uri("/gate/passages/today")
                    .retrieve()
                    .body(PassageDTO[].class);
            return (response != null) ? Arrays.asList(response) : List.of();
        } catch (Exception e){
            return List.of();
        }
    }

    public List<Passage> collectPassagesFromAllGates(){
        return gateUrls.entrySet().stream()
                .flatMap(entry -> {
                    String gateId = entry.getKey();
                    String url = entry.getValue();
                    return getPassagesofToday(url).stream()
                            .map(dto -> new Passage(dto.nfcCardId(), dto.timestamp(), gateId));
                })
                .toList();
    }

    public List<PassageDTO> getPassagesofTodayForGate(String code) {
        String targetUrl = getUrl(code);

        try{
            return Arrays.stream(Objects.requireNonNull(restClientBuilder.baseUrl(targetUrl).build().get().uri("/gate/passages/today").retrieve().body(PassageDTO[].class))).toList();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public java.util.Set<String> getAllGateIds() {
        return gateUrls.keySet();
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

    public void syncTodayPassages(String gateId) {
        String targetUrl = getUrl(gateId);
        try {
            PassageDTO[] doorPassages = restClientBuilder.build().get().uri(targetUrl + "/gate/passages/today").retrieve().body(PassageDTO[].class);

            if (doorPassages != null) {
                List<Passage> passagesToSave = Arrays.stream(doorPassages)
                        .map(dto ->{
                            System.out.println("DTO reçu -> ID: " + dto.nfcCardId() + " Date: " + dto.timestamp());
                            return new Passage(dto.nfcCardId(), dto.timestamp(),gateId);
                        }).toList();

                // Note: You might want to check for duplicates before saving
                // if you call this method multiple times a day!
                passageRepository.saveAll(passagesToSave);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }


    public void syncAllGates(List<Long> cardIds){
        gateUrls.values().forEach(url -> syncAuthorizedCards(url, cardIds));
    }

    public void sendOrder(String code, String order) {
        sendOrderToUrl(getUrl(code), order);
    }

    private void sendOrderToUrl(String baseUrl, String order) {
        restClientBuilder.baseUrl(baseUrl).build()
                .post()
                .uri("/gate" + order)
                .retrieve()
                .toBodilessEntity();
    }

    public GateStatusResponseDTO getGateStatus(String code){
        String targetUrl = getUrl(code);

        return restClientBuilder.baseUrl(targetUrl).build()
                .get()
                .uri("/gate/status")
                .retrieve()
                .body(GateStatusResponseDTO.class);
    }

    public long syncOpeningTimeByGate(String gateId) {
        String targetUrl = getUrl(gateId);
        try {
            Long result = restClientBuilder.baseUrl(targetUrl)
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
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du temps d'ouverture pour " + targetUrl + ": " + e.getMessage());
            return -1L;
        }
    }

    public void updateAlertThreshold(String code, int alertThreshold) {
        String targetUrl = getUrl(code);

        restClientBuilder.baseUrl(targetUrl).build()
                .put()
                .uri("/gate/alert-threshold")
                .body(new GateAlertThresholdUpdateDTO(alertThreshold))
                .retrieve()
                .toBodilessEntity();
    }

    public List<String> getConfiguredGateCodes() {
        return gateUrls.keySet().stream().sorted().toList();
    }

    public void sendOrderAll(String order){
        gateUrls.values().forEach(url -> {
            try {
                sendOrderToUrl(url, order);
            } catch (HttpClientErrorException.Conflict ignored) {
                // Gate already in requested state, skip
            }
        });
    }

    public void setAlertThreshold(String baseUrl, int alertThreshold) {
        restClientBuilder.baseUrl(baseUrl).build()
                .put()
                .uri("/gate/alert-threshold")
                .body(new GateAlertThresholdUpdateDTO(alertThreshold))
                .retrieve()
                .toBodilessEntity();
    }

    private String getUrl(String code){
        String url = gateUrls.get(code);
        if (url == null) throw new NoGateExistentException("Code de porte inconnu : " + code);
        return url;
    }

}
