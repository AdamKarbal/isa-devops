package skimaster.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import skimaster.dtos.DisplayAlertRequestDTO;

import java.util.List;

@Component
public class DisplayNotifierClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayNotifierClient.class);
    private final RestClient.Builder restClientBuilder;
    private final List<String> displayUrls;

    public DisplayNotifierClient(@Value("${display.host.baseurl:http://localhost:8083}") String displayHost,
                                 @Value("${display.host2.baseurl:http://localhost:8087}") String displayHost2,
                                 RestClient.Builder builder) {
        this.restClientBuilder = builder;
        this.displayUrls = List.of(displayHost, displayHost2);
    }

    public void publishAlert(String message, String gateCode) {
        displayUrls.forEach(url -> {
            try {
                restClientBuilder.baseUrl(url).build()
                        .post()
                        .uri("/display/alerts")
                        .body(new DisplayAlertRequestDTO(message, gateCode))
                        .retrieve()
                        .toBodilessEntity();
            } catch (Exception e) {
                LOGGER.warn("Unable to notify display {} for gate {}: {}", url, gateCode, e.getMessage());
            }
        });
    }
}
