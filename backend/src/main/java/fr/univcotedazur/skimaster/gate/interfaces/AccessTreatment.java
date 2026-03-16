package fr.univcotedazur.skimaster.gate.interfaces;

import java.util.List;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.gate.dtos.AccessRequest;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;

public interface AccessTreatment {

    void pushAuthorizedBadges();
    void processReceivedPassages(List<Long> nfcCardIds);
}
