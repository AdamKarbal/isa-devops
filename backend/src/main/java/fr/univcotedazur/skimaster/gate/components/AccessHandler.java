package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.forfait.components.ForfaitRegistry;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.gate.interfaces.AccessDealer;
import fr.univcotedazur.skimaster.gate.interfaces.AccessTreatment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccessHandler implements AccessDealer {

    private final AccessTreatment passService;
    private final ForfaitRegistry forfaitRegistry;
    private final WhiteListCache whiteListCache;
    private final Portier portier;

    public AccessHandler(AccessTreatment passService,
                         ForfaitRegistry forfaitRegistry,
                         WhiteListCache whiteListCache,
                         Portier portier) {
        this.passService = passService;
        this.forfaitRegistry = forfaitRegistry;
        this.whiteListCache = whiteListCache;
        this.portier = portier;
    }

    @Override
    public void synchronizeExternalSystems() {
        passService.pushAuthorizedBadges();
    }
    @Override
    public List<Long> handleIncomingData() { // Change void en List<Long>
        List<Long> allNfcCardIds = portier.collectPassagesFromAllGates();
        passService.processReceivedPassages(allNfcCardIds);
        return allNfcCardIds; // Retourne la liste à la CLI
    }
}
