package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.customer.components.CustomerRegistry;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;
import fr.univcotedazur.skimaster.gate.interfaces.AccessTreatment;
import fr.univcotedazur.skimaster.gate.interfaces.BeepInformation;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class PassService implements AccessTreatment {
    private final BeepInformation beepInformation;
    private final CustomerRegistry customerRegistry;
    private final WhiteListCache whiteListCache;

    public PassService(BeepInformation beepInformation, CustomerRegistry customerRegistry, WhiteListCache whiteListCache) {
        this.beepInformation = beepInformation;
        this.customerRegistry = customerRegistry;
        this.whiteListCache = whiteListCache;
    }

    public void validateAndRegisterPassage(BeepContext beepContext) throws CustomerIdNotFoundException {
        Optional<Forfait> forfeitOpt = whiteListCache.getForfeitList().stream()
                .filter(forfeit -> Objects.equals(forfeit.getId(), beepContext.cardId()))
                .findFirst();
        if (forfeitOpt.isPresent()) {
            Optional<Customer> customer = customerRegistry.findById(forfeitOpt.get().getCustomerId());
            //register into PassageRepo
            customer.ifPresent(value -> beepInformation.assignBeep(beepContext, forfeitOpt.get().getAgeCategory()));
        } else throw new CustomerIdNotFoundException();
    }
}
