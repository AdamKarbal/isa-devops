package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.forfait.components.ForfaitRegistry;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.gate.components.WhiteListCache;
import fr.univcotedazur.skimaster.gate.dtos.AccessRequest;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;
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

    public AccessHandler(AccessTreatment passService, ForfaitRegistry forfaitRegistry, WhiteListCache whiteListCache) {
        this.passService = passService;
        this.forfaitRegistry = forfaitRegistry;
        this.whiteListCache = whiteListCache;
    }

    @Override
    public void computeWhiteList() {
        List<Forfait> forfaits = forfaitRegistry.findAllForfaits().stream()
                .filter(forfait -> !forfait.getValidDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
        whiteListCache.addForfeits(forfaits);
    }


    public void synchronizeExternalSystems() {
        passService.pushAuthorizedBadges();
    }

    public void handleIncomingData(List<Long> nfcCardIds) {
        passService.processReceivedPassages(nfcCardIds);
    }
}
