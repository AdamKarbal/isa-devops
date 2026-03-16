package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.PassageDTO;
import fr.univcotedazur.skimaster.gate.dtos.AccessRequest;
import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.exceptions.GateClosedException;
import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;

@Component
public class Portier implements ManualGateControl {

    private final TurnstileProxy turnstileProxy;

    public Portier(TurnstileProxy turnstileProxy) {
        this.turnstileProxy = turnstileProxy;
    }

    public void syncAllGates(List<Long> cardIds) {
       turnstileProxy.syncAllGates(cardIds);
    }

    public List<Passage> collectPassagesFromAllGates(){
        return gateUrls.entrySet().stream()
                .flatMap(entry -> {
                    String gateId = entry.getKey();
                    String url = entry.getValue();
                    return turnstileProxy.getPassagesofToday(url).stream()
                            .map(dto -> new Passage(dto.nfcCardId(), dto.timestamp(), gateId));
                })
                .toList();
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
