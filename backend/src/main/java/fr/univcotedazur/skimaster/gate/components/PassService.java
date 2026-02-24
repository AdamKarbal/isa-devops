package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.customer.components.CustomerRegistry;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;
import fr.univcotedazur.skimaster.gate.interfaces.AccessTreatment;
import fr.univcotedazur.skimaster.gate.interfaces.BeepInformation;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PassService implements AccessTreatment {
    private final BeepInformation beepInformation;
    private final CustomerRegistry customerRegistry;
    private WhiteListCache whiteListCache;

    public PassService(BeepInformation beepInformation, CustomerRegistry customerRegistry,WhiteListCache whiteListCache) {
        this.beepInformation = beepInformation;
        this.customerRegistry = customerRegistry;
        this.whiteListCache = whiteListCache;
    }

    public void validateAndRegisterPassage(BeepContext beepContext)throws CustomerIdNotFoundException{
        if(whiteListCache.getForfeitList().stream().anyMatch(forfeit -> forfeit.getId()==beepContext.cardId())){
            Optional<Customer> customer =customerRegistry.findById(beepContext.cardId());//maybe forfeitRepo and then customer
            //register intoPassageRepo
            customer.ifPresent(value -> beepInformation.assignBeep(beepContext, value.getAge()));
        }

        else throw new CustomerIdNotFoundException();
    }
}
