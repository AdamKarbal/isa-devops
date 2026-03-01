package fr.univcotedazur.skimaster.gate.interfaces;

import java.util.List;

public interface AccessTreatment {
    void pushAuthorizedBadges();
    void processReceivedPassages(List<Long> nfcCardIds);
}
