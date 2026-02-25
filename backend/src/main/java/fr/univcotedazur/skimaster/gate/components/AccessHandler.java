package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.forfait.components.ForfeitRegistry;
import fr.univcotedazur.skimaster.forfait.entities.Forfeit;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;
import fr.univcotedazur.skimaster.gate.interfaces.AccessDealer;
import fr.univcotedazur.skimaster.gate.interfaces.AccessTreatment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccessHandler implements AccessDealer {

    private final AccessTreatment treatment;
    private final ForfeitRegistry forfeitRegistry;
    private final WhiteListCache whiteListCache;

    public AccessHandler(AccessTreatment treatment, ForfeitRegistry forfeitRegistry, WhiteListCache whiteListCache) {
        this.treatment = treatment;
        this.forfeitRegistry = forfeitRegistry;
        this.whiteListCache = whiteListCache;
    }

    @Override
    public void fetchRecentPassage(BeepContext beepContext) throws CustomerIdNotFoundException {
        treatment.validateAndRegisterPassage(beepContext);
    }

    @Override
    public void computeWhiteList() {
        List<Forfeit> forfeits = forfeitRegistry.findAllForfaits().stream().filter(forfeit -> forfeit.getValidDate().isAfter(LocalDate.now())).collect(Collectors.toList());
        whiteListCache.addForfeits(forfeits);
    }
}
