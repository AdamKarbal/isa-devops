package fr.univcotedazur.skimaster.gate.connectors;

import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateStatusResponseDTO;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateSyncDTO;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.PassageDTO;
import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.gate.repositories.PassageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class TurnstileProxy {

    private final RestClient.Builder restClientBuilder;
    private final PassageRepository passageRepository;
    @Value("${DOOR_WITH_PORT}")
    private String doorBaseUrl;

    public TurnstileProxy(RestClient.Builder builder, PassageRepository passageRepository) {
        this.restClientBuilder = builder;
        this.passageRepository = passageRepository;
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

    public void syncTodayPassages() {
        System.out.println("URL DOOR-1 : " + doorBaseUrl);
        try {
            String url = doorBaseUrl + "/gate/passages/today";

            PassageDTO[] doorPassages = restClientBuilder.build().get().uri(url).retrieve().body(PassageDTO[].class);

            if (doorPassages != null) {
                List<Passage> passagesToSave = Arrays.stream(doorPassages)
                        .map(dto ->{
                                System.out.println("DTO reçu -> ID: " + dto.nfcCardId() + " Date: " + dto.timestamp());
                                return new Passage(dto.nfcCardId(), dto.timestamp());
                        }).toList();

                // Note: You might want to check for duplicates before saving
                // if you call this method multiple times a day!
                passageRepository.saveAll(passagesToSave);
            }
        }catch(Exception e){
            System.out.println(e);
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
            String url = doorBaseUrl + "/gate/opening-time/";
            Long res = restClientBuilder.build().get().uri(url+ gateId).retrieve().body(Long.class);
            return (res != null) ? res : -1L;
        }catch (Exception e) {
            System.out.println(e);
            return -1L;
        }
    }



}
