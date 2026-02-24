package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.cart.components.CartHandler;
import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cart.repositories.ProductRepository;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerRegistration;
import fr.univcotedazur.skimaster.forfait.components.ForfeitRegistry;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfeit;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.repositories.ForfeitOfferRepository;
import fr.univcotedazur.skimaster.forfait.repositories.ForfeitRepository;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;
import fr.univcotedazur.skimaster.gate.interfaces.AccessDealer;
import fr.univcotedazur.skimaster.order.entities.Order;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccessHandlerTest {

    @Autowired
    private CustomerRegistration customerRegistration;

    @Autowired
    private AccessHandler accessHandler;

    @Autowired
    private ForfeitOfferRepository forfeitOfferRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WhiteListCache whiteListCache;
    @Autowired
    private ForfeitRepository forfeitRepository;
    private Customer returned;
    private ForfeitOffer adultForfait;
    private ForfeitOffer childForfait;
    private Product childProduct;
    private Product adultProduct;

    @BeforeEach
    void setup() throws AlreadyExistingCustomerException {
        adultForfait = new ForfeitOffer(AgeCategory.ADULT, PassType.FULL, 10.0);
        childForfait = new ForfeitOffer(AgeCategory.CHILD, PassType.FULL, 5.0);

        adultForfait = forfeitOfferRepository.save(adultForfait);
        childForfait = forfeitOfferRepository.save(childForfait);

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
        Forfeit forfeitChild = new Forfeit(returned.getId(), LocalDate.now().plusDays(1),childForfait);
        forfeitRepository.save(forfeitChild);
        BeepContext beepChild = new BeepContext(forfeitChild.getId(),12L, LocalDateTime.now());
        accessHandler.computeWhiteList();
        assertDoesNotThrow(()->accessHandler.fetchRecentPassage(beepChild));
        assertEquals(1, whiteListCache.getForfeitList().size());
        assertEquals(forfeitChild.getId(),whiteListCache.getValidForfeitIds().getFirst());

    }

    @Test
    void testAdultBeep(){
        Forfeit forfeitAdult = new Forfeit(returned.getId(), LocalDate.now().plusDays(1),adultForfait);
        forfeitRepository.save(forfeitAdult);
        BeepContext beepAdult = new BeepContext(forfeitAdult.getId(),14L, LocalDateTime.now());
        accessHandler.computeWhiteList();
        assertDoesNotThrow(()->accessHandler.fetchRecentPassage(beepAdult));
        assertEquals(1, whiteListCache.getForfeitList().size());
        assertEquals(forfeitAdult.getId(),whiteListCache.getValidForfeitIds().getFirst());
    }

}
