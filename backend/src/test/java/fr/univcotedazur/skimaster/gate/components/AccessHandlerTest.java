package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cart.repositories.ProductRepository;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerRegistration;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitRepository;
import fr.univcotedazur.skimaster.forfait.repositories.ForfeitOfferRepository;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccessHandlerTest {

    @Autowired
    private CustomerRegistration customerRegistration;

    @Autowired
    private AccessHandler accessHandler;

    @Autowired
    private ForfeitOfferRepository forfaitOfferRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WhiteListCache whiteListCache;
    @Autowired
    private ForfaitRepository ForfaitRepository;
    private Customer returned;
    private ForfeitOffer adultForfait;
    private ForfeitOffer childForfait;
    private Product childProduct;
    private Product adultProduct;

    @BeforeEach
    void setup() throws AlreadyExistingCustomerException {
        adultForfait = new ForfeitOffer(AgeCategory.ADULT, PassType.FULL, 10.0);
        childForfait = new ForfeitOffer(AgeCategory.CHILD, PassType.FULL, 5.0);

        adultForfait = forfaitOfferRepository.save(adultForfait);
        childForfait = forfaitOfferRepository.save(childForfait);

        childProduct = new Product(List.of(childForfait));
        adultProduct = new Product(List.of(adultForfait));

        productRepository.save(childProduct);
        productRepository.save(adultProduct);

        String name = "John";
        String creditCard = "1234567890";
        returned = customerRegistration.register(name, creditCard);
    }

    @Test
    void testChildBeep(){
        Forfait forfaitChild = new Forfait(returned.getId(), LocalDate.now().plusDays(1),childForfait);
        ForfaitRepository.save(forfaitChild);
        BeepContext beepChild = new BeepContext(forfaitChild.getId(),"gate-1", LocalDateTime.now());
        accessHandler.computeWhiteList();
        assertEquals(forfaitChild.getId(),accessHandler.processCurrentPassage(beepChild).forfeitId());
        assertEquals(BeepSignal.CHILD_BEEP,accessHandler.processCurrentPassage(beepChild).beepSignal());
        assertTrue(accessHandler.processCurrentPassage(beepChild).isAuthorized());
        assertEquals(1, whiteListCache.getForfeitList().size());
        assertEquals(forfaitChild.getId(),whiteListCache.getValidForfeitIds().getFirst());

    }

    @Test
    void testAdultBeep(){
        Forfait forfaitAdult = new Forfait(returned.getId(), LocalDate.now().plusDays(1),adultForfait);
        ForfaitRepository.save(forfaitAdult);
        BeepContext beepAdult = new BeepContext(forfaitAdult.getId(),"gate-1", LocalDateTime.now());
        accessHandler.computeWhiteList();
        assertEquals(forfaitAdult.getId(),accessHandler.processCurrentPassage(beepAdult).forfeitId());
        assertEquals(BeepSignal.STANDARD_BEEP,accessHandler.processCurrentPassage(beepAdult).beepSignal());
        assertTrue(accessHandler.processCurrentPassage(beepAdult).isAuthorized());
        assertEquals(1, whiteListCache.getForfeitList().size());
        assertEquals(forfaitAdult.getId(),whiteListCache.getValidForfeitIds().getFirst());
    }

}
