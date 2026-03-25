package fr.univcotedazur.skimaster.admin.components;

import fr.univcotedazur.skimaster.admin.dto.DisplayAlertDTO;
import fr.univcotedazur.skimaster.incident.connectors.externaldto.DisplayAlertRequestDTO;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DisplayAdminProxy {

    private final RestClient.Builder restClientBuilder;
    private final Map<String, String> displayUrls;

    public DisplayAdminProxy(RestClient.Builder builder, Environment environment) {
        this.restClientBuilder = builder;
        this.displayUrls = Binder.get(environment)
                .bind("display.instances", Bindable.mapOf(String.class, String.class))
                .orElseGet(Map::of);
    }

    public void sendMessage(String message, String displayCode) {
        String target = (displayCode == null || displayCode.isBlank()) ? "ADMIN" : displayCode;
        displayUrls.values().forEach(url ->
                restClientBuilder.baseUrl(url).build()
                        .post()
                        .uri("/display/alerts")
                        .body(new DisplayAlertRequestDTO(message, target))
                        .retrieve()
                        .toBodilessEntity()
        );
    }

    public void clear() {
        displayUrls.values().forEach(url ->
                restClientBuilder.baseUrl(url).build()
                        .delete()
                        .uri("/display/alerts")
                        .retrieve()
                        .toBodilessEntity()
        );
    }

    public void clearForGate(String gateCode) {
        displayUrls.values().forEach(url ->
                restClientBuilder.baseUrl(url).build()
                        .delete()
                        .uri("/display/alerts/{gateCode}", gateCode)
                        .retrieve()
                        .toBodilessEntity()
        );
    }

    public List<DisplayAlertDTO> getAlerts() {
        List<DisplayAlertDTO> all = new ArrayList<>();
        displayUrls.values().forEach(url -> {
            List<DisplayAlertDTO> alerts = restClientBuilder.baseUrl(url).build()
                    .get()
                    .uri("/display/alerts")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<DisplayAlertDTO>>() {});
            if (alerts != null) all.addAll(alerts);
        });
        return all;
    }

    public List<DisplayAlertDTO> getAlerts(String displayCode) {
        List<DisplayAlertDTO> all = new ArrayList<>();
        displayUrls.values().forEach(url -> {
            List<DisplayAlertDTO> alerts = restClientBuilder.baseUrl(url).build()
                    .get()
                    .uri("/display/alerts/{displayCode}", displayCode)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<DisplayAlertDTO>>() {});
            if (alerts != null) all.addAll(alerts);
        });
        return all;
    }

    public List<String> getDisplayList() {
        List<String> all = new ArrayList<>();
        displayUrls.values().forEach(url -> {
            List<String> codes = restClientBuilder.baseUrl(url).build()
                    .get()
                    .uri("/display/list")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<String>>() {});
            if (codes != null) all.addAll(codes);
        });
        return all.stream().distinct().toList();
    }

    public List<String> getConfiguredDisplayCodes() {
        return displayUrls.keySet().stream().sorted().toList();
    }
}
