package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateStatusResponseDTO;
import fr.univcotedazur.skimaster.gate.dtos.AccessRequest;
import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Portier implements ManualGateControl, PortierInformation {
    private static final Logger LOG = LoggerFactory.getLogger(Portier.class);

    private final TurnstileProxy turnstileProxy;

    public Portier(TurnstileProxy turnstileProxy) {
        this.turnstileProxy = turnstileProxy;
    }

    public void syncAllGates(List<Long> cardIds) {
        LOG.info("[Portier] Diffusion de la mise à jour aux portails ({} IDs en Whitelist)", cardIds.size());
        turnstileProxy.syncAllGates(cardIds);
    }


    public List<Passage> collectPassagesFromAllGates(){
        LOG.info("[PORTIER] Interrogation de tous les portails pour collecter les passages du jour...");
        List<Passage> totalPassages = turnstileProxy.collectPassagesFromAllGates();
        LOG.info("[PORTIER] Collecte globale terminée. Passages récupérés : {}", totalPassages.size());
        return totalPassages;
    }

    @Override
    public GateStatusResponseDTO gateStatus(String code) {
        return turnstileProxy.getGateStatus(code);
    }

    @Override
    public void closeGate(String code) {
        turnstileProxy.sendOrder(code, "/close");
    }

    @Override
    public void openGate(String code) {
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

    public long getOpeningTime(String code) {
        return turnstileProxy.syncOpeningTimeByGate(code);
    }

    @Override
    public void setAlertThreshold(String code, int alertThreshold) {
        turnstileProxy.updateAlertThreshold(code, alertThreshold);
    }

    @Override
    public List<String> getConfiguredGateCodes() {
        return turnstileProxy.getConfiguredGateCodes();
    }

    @Override
    public AccessRequest authorizePassage(String code, Long forfeitId, BeepSignal signal) {
        return new AccessRequest(forfeitId, signal, true);
    }

    @Override
    public AccessRequest denyPassage(String code) {
        return new AccessRequest(null, BeepSignal.ACCESS_DENIED, false);
    }
}
