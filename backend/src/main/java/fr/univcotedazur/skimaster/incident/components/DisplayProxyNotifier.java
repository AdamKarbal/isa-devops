package fr.univcotedazur.skimaster.incident.components;

import fr.univcotedazur.skimaster.incident.connectors.externaldto.DisplayAlertRequestDTO;
import fr.univcotedazur.skimaster.incident.interfaces.DisplayNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DisplayProxyNotifier implements DisplayNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayProxyNotifier.class);
    private final RestClient restClient;

    public DisplayProxyNotifier(@Value("${display.host.baseurl:http://localhost:8083}") String displayHost, RestClient.Builder builder) {
        this.restClient = builder.baseUrl(displayHost).build();
    }

    @Override
    public void publishIncidentAlert(String message, String gateCode) {
        try {
            restClient.post()
                    .uri("/display/alerts")
                    .body(new DisplayAlertRequestDTO(message, gateCode))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            LOGGER.warn("Unable to notify display service for gate {}: {}", gateCode, e.getMessage());
        }
    }
}
