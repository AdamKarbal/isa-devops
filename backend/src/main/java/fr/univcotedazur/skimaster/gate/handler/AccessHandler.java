package fr.univcotedazur.skimaster.gate.handler;

import fr.univcotedazur.skimaster.gate.interfaces.AccessTreatment;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AccessHandler {

    private final AccessTreatment passService;

    public AccessHandler(AccessTreatment passService) {
        this.passService = passService;
    }

    public void synchronizeExternalSystems() {
        passService.pushAuthorizedBadges();
    }

    public void handleIncomingData(List<Long> nfcCardIds) {
        passService.processReceivedPassages(nfcCardIds);
    }
}
