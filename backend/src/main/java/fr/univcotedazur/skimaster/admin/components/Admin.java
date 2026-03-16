package fr.univcotedazur.skimaster.admin.components;

import fr.univcotedazur.skimaster.admin.interfaces.AdminOperations;
import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import fr.univcotedazur.skimaster.inbox.interfaces.MessageInput;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Admin implements AdminOperations {

    private final ManualGateControl manualGateControl;
    private final MessageInput messageInput;

    public Admin(ManualGateControl manualGateControl, MessageInput messageInput) {
        this.manualGateControl = manualGateControl;
        this.messageInput = messageInput;
    }

    @Override
    public void setStatus(String code, String status) {
        if (Objects.equals(status, "OPEN")) {
            manualGateControl.openGate(code);
        } else if (Objects.equals(status, "CLOSE")) {
            manualGateControl.closeGate(code);
        }
    }

    @Override
    public void sendMessage(String msg) {
        messageInput.sendMessage(msg);
    }

    @Override
    public void clearPannel() {
        messageInput.clearPannel();
    }

    @Override
    public void openAllGates() {
        manualGateControl.openAllGates();
    }

    @Override
    public void closeAllGates() {
        manualGateControl.closeAllGates();
    }
}
