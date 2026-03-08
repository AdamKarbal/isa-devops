package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import fr.univcotedazur.skimaster.gate.dtos.AccessRequest;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.exceptions.GateClosedException;
import fr.univcotedazur.skimaster.gate.exceptions.NoGateExistentException;
import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class Portier implements ManualGateControl, PortierInformation {

    private final TurnstileProxy turnstileProxy;

    @Value("#{${gate.instances}}")
    private Map<String, String> gateUrls;

    public Portier(TurnstileProxy turnstileProxy) {
        this.turnstileProxy = turnstileProxy;
    }

    public void syncAllGates(List<Long> cardIds) {
        gateUrls.values().forEach(url -> {
            turnstileProxy.syncAuthorizedCards(url, cardIds);
        });
    }

    @Override
    public void closeGate(String code){
        String url = getUrl(code);
        turnstileProxy.sendOrder(url, "/close");
    }

    @Override
    public void openGate(String code){
        String url = getUrl(code);
        turnstileProxy.sendOrder(url, "/open");
    }

    @Override
    public void openAllGates() {
        gateUrls.values().forEach(url -> turnstileProxy.sendOrder(url, "/open"));
    }

    @Override
    public void closeAllGates() {
        gateUrls.values().forEach(url -> turnstileProxy.sendOrder(url, "/close"));
    }

    @Override
    public AccessRequest authorizePassage(String code, Long forfeitId, BeepSignal signal) {
        if(Objects.equals(turnstileProxy.getGateStatus(getUrl(code)).status(), "OPEN")){
            return new AccessRequest(forfeitId,signal,true);
        }
        throw new GateClosedException("Vous ne pouvez pas autoriser de passage sur un portique fermé.");
    }

    @Override
    public AccessRequest denyPassage(String code) {
        if(Objects.equals(turnstileProxy.getGateStatus(getUrl(code)).status(), "CLOSED")){
            return new AccessRequest(null,BeepSignal.ACCESS_DENIED,false);
        }
        throw new GateClosedException("Vous ne pouvez pas refuser de passage sur un portique fermé.");
    }

    private String getUrl(String code){
        String url = gateUrls.get(code);
        if (url == null) throw new NoGateExistentException("Code de porte inconnu : " + code);
        return url;
    }
}
