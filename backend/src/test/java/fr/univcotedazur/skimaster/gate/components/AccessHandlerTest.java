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
import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
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
    private ForfaitRepository forfaitRepository;

    @MockitoBean
    private TurnstileProxy turnstileProxy;

    private Customer returned;
    private ForfeitOffer adultForfait;
    private ForfeitOffer childForfait;

    @BeforeEach
    void setup() throws AlreadyExistingCustomerException {
        whiteListCache.getForfeitList().clear();

        adultForfait = new ForfeitOffer(AgeCategory.ADULT, PassType.FULL, 10.0);
        childForfait = new ForfeitOffer(AgeCategory.CHILD, PassType.FULL, 5.0);

        adultForfait = forfaitOfferRepository.save(adultForfait);
        childForfait = forfaitOfferRepository.save(childForfait);

        productRepository.save(new Product(List.of(childForfait)));
        productRepository.save(new Product(List.of(adultForfait)));

        returned = customerRegistration.register("John", "1234567890");
    }

    @Test
    void testComputeWhiteListWithChildForfait(){
        Forfait forfaitChild = new Forfait(returned.getId(), LocalDate.now().plusDays(1), childForfait);
        forfaitRepository.save(forfaitChild);

        accessHandler.computeWhiteList();

        assertEquals(1, whiteListCache.getForfeitList().size());
        assertEquals(forfaitChild.getId(), whiteListCache.getValidForfeitIds().get(0));
        assertEquals(AgeCategory.CHILD, whiteListCache.getForfeitList().get(0).getAgeCategory());
    }

    @Test
    void testComputeWhiteListWithAdultForfait(){
        Forfait forfaitAdult = new Forfait(returned.getId(), LocalDate.now().plusDays(1), adultForfait);
        forfaitRepository.save(forfaitAdult);

        accessHandler.computeWhiteList();

        assertEquals(1, whiteListCache.getForfeitList().size());
        assertEquals(forfaitAdult.getId(), whiteListCache.getValidForfeitIds().get(0));
    }

    @Test
    void testComputeWhiteListFiltersExpiredForfait(){
        Forfait expired = new Forfait(returned.getId(), LocalDate.now().minusDays(1), adultForfait);
        forfaitRepository.save(expired);

        accessHandler.computeWhiteList();

        assertTrue(whiteListCache.getForfeitList().isEmpty(), "Le forfait expiré ne doit pas être dans la whitelist");
    }
}