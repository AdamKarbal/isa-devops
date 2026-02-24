package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.gate.entities.Forfeit;
import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.gate.interfaces.AccessDealer;
import fr.univcotedazur.skimaster.gate.interfaces.AccessTreatment;
import fr.univcotedazur.skimaster.gate.repositories.ForfeitRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccessHandler implements AccessDealer {

    private final AccessTreatment treatment;
    private final ForfeitRepository forfeitRepository;
    private final WhiteListCache whiteListCache;

    public AccessHandler(AccessTreatment treatment, ForfeitRepository forfeitRepository, WhiteListCache whiteListCache) {
        this.treatment = treatment;
        this.forfeitRepository = forfeitRepository;
        this.whiteListCache = whiteListCache;
    }

    @Override
    public List<Passage> fetchRecentPassages() {
        return null;
    }

    @Override
    public void sendWhiteListToGates() {
        List<Forfeit> forfeits = forfeitRepository.getAllForfaits();
        whiteListCache.addForfeits(forfeits);
    }
}
