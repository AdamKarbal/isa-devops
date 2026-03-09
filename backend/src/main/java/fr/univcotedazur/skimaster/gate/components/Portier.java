package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateStatusResponseDTO;
import fr.univcotedazur.skimaster.gate.dtos.AccessRequest;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.exceptions.GateClosedException;
import fr.univcotedazur.skimaster.gate.exceptions.NoGateExistentException;
import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class Portier implements ManualGateControl, PortierInformation {

    private final TurnstileProxy turnstileProxy;
    private final Map<String, String> gateUrls;

    public Portier(TurnstileProxy turnstileProxy, Environment environment) {
        this.turnstileProxy = turnstileProxy;
        this.gateUrls = Binder.get(environment)
                .bind("gate.instances", Bindable.mapOf(String.class, String.class))
                .orElseGet(Map::of);
    }

    public void syncAllGates(List<Long> cardIds) {
        gateUrls.values().forEach(url -> {
            turnstileProxy.syncAuthorizedCards(url, cardIds);
        });
    }

    public List<String> getConfiguredGateCodes() {
        return gateUrls.keySet().stream().sorted().toList();
    }

    public GateStatusResponseDTO gateStatus(String code) {
        return turnstileProxy.getGateStatus(getUrl(code));
    }

    public void setAlertThreshold(String code, int alertThreshold) {
        turnstileProxy.setAlertThreshold(getUrl(code), alertThreshold);
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
