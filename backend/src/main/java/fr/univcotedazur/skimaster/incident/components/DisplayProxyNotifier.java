package fr.univcotedazur.skimaster.incident.components;

import fr.univcotedazur.skimaster.admin.components.DisplayAdminProxy;
import fr.univcotedazur.skimaster.incident.interfaces.DisplayNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DisplayProxyNotifier implements DisplayNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayProxyNotifier.class);
    private final DisplayAdminProxy displayAdminProxy;

    public DisplayProxyNotifier(DisplayAdminProxy displayAdminProxy) {
        this.displayAdminProxy = displayAdminProxy;
    }

    @Override
    public void publishIncidentAlert(String message, String gateCode) {
        try {
            displayAdminProxy.sendMessage(message, gateCode);
        } catch (Exception e) {
            LOGGER.warn("Unable to notify display service for gate {}: {}", gateCode, e.getMessage());
        }
    }
}
