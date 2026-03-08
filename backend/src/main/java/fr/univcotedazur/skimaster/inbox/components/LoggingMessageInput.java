package fr.univcotedazur.skimaster.inbox.components;

import fr.univcotedazur.skimaster.inbox.interfaces.MessageInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingMessageInput implements MessageInput {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingMessageInput.class);

    @Override
    public void sendMessage(String msg) {
        LOGGER.info("Admin message sent: {}", msg);
    }

    @Override
    public void clearPannel() {
        LOGGER.info("Admin message panel cleared");
    }
}
