package fr.univcotedazur.skimaster.gate.interfaces;

import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;

public interface AccessTreatment {

    void validateAndRegisterPassage(BeepContext beepContext) throws CustomerIdNotFoundException;
}
