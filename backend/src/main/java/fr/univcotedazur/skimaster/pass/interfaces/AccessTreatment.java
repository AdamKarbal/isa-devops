package fr.univcotedazur.skimaster.pass.interfaces;

import java.util.List;

public interface AccessTreatment {
    void pushAuthorizedBadges();
    void processReceivedPassages(List<Long> nfcCardIds);
}
