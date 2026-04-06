package fr.univcotedazur.skimaster.inbox.components;

import fr.univcotedazur.skimaster.inbox.interfaces.MessageInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

record DisplayAlertRequestDTO(String message, String gateCode) {}

@Component
public class LoggingMessageInput implements MessageInput {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingMessageInput.class);
    private final RestClient.Builder restClientBuilder;
    private final Map<String, String> displayUrls;

    public LoggingMessageInput(RestClient.Builder builder, Environment environment) {
        this.restClientBuilder = builder;
        this.displayUrls = Binder.get(environment)
                .bind("display.instances", Bindable.mapOf(String.class, String.class))
                .orElseGet(Map::of);
    }

    @Override
    public void sendMessage(String msg) {
        displayUrls.values().forEach(url -> {
            try {
                restClientBuilder.baseUrl(url).build()
                        .post()
                        .uri("/display/alerts")
                        .body(new DisplayAlertRequestDTO(msg, "ADMIN"))
                        .retrieve()
                        .toBodilessEntity();
            } catch (Exception e) {
                LOGGER.warn("Unable to forward admin message to display {}: {}", url, e.getMessage());
            }
        });
        LOGGER.info("Admin message sent: {}", msg);
    }

    @Override
    public void clearPannel() {
        displayUrls.values().forEach(url -> {
            try {
                restClientBuilder.baseUrl(url).build()
                        .delete()
                        .uri("/display/alerts")
                        .retrieve()
                        .toBodilessEntity();
            } catch (Exception e) {
                LOGGER.warn("Unable to clear display panel {}: {}", url, e.getMessage());
            }
        });
        LOGGER.info("Admin message panel cleared");
    }
}
