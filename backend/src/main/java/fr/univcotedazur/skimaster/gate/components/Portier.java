package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class Portier implements ManualGateControl {

    private final TurnstileProxy turnstileProxy;

    public Portier(TurnstileProxy turnstileProxy) {
        this.turnstileProxy = turnstileProxy;
    }

    public void syncAllGates(List<Long> cardIds) {
       turnstileProxy.syncAllGates(cardIds);
    }

    @Override
    public void closeGate(String code){
        turnstileProxy.sendOrder(code, "/close");
    }

    @Override
    public void openGate(String code){
        turnstileProxy.sendOrder(code, "/open");
    }

    @Override
    public void openAllGates() {
        turnstileProxy.sendOrderAll("/open");
    }

    @Override
    public void closeAllGates() {
        turnstileProxy.sendOrderAll("/close");
    }

    public long getOpeningTime(String code){
        return turnstileProxy.syncOpeningTimeByGate(code);
    }
}
