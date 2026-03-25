package skimaster.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import skimaster.dtos.DisplayAlertRequestDTO;

@Component
public class DisplayNotifierClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayNotifierClient.class);
    private final RestClient restClient;

    public DisplayNotifierClient(@Value("${display.host.baseurl:http://localhost:8083}") String displayHost,
                                 RestClient.Builder builder) {
        this.restClient = builder.baseUrl(displayHost).build();
    }

    public void publishAlert(String message, String gateCode) {
        try {
            restClient.post()
                    .uri("/display/alerts")
                    .body(new DisplayAlertRequestDTO(message, gateCode))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            LOGGER.warn("Unable to notify display for gate {}: {}", gateCode, e.getMessage());
        }
    }
}
