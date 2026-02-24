package fr.univcotedazur.skimaster.cart.components;

import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cart.repositories.ProductRepository;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerRegistration;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.ForfaitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitOfferRepository;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CartTest {

    @Autowired
    private CustomerRegistration customerRegistration;

    @Autowired
    private CartHandler cartHandler;

    @Autowired
    private ForfaitOfferRepository forfaitOfferRepository;

    @Autowired
    private ProductRepository productRepository;

    private Customer returned;
    private ForfaitOffer adultForfait;
    private ForfaitOffer childForfait;
    private Product childAdultProduct;

    private final String name = "John";
    private final String creditCard = "1234567890";

    @BeforeEach
    void setup() throws AlreadyExistingCustomerException {
        adultForfait = new ForfaitOffer(AgeCategory.ADULT, PassType.FULL, 10.0);
        childForfait = new ForfaitOffer(AgeCategory.CHILD, PassType.FULL, 5.0);

        adultForfait = forfaitOfferRepository.save(adultForfait);
        childForfait = forfaitOfferRepository.save(childForfait);

        childAdultProduct = new Product(List.of(adultForfait, childForfait));
        childAdultProduct = productRepository.save(childAdultProduct);

        returned = customerRegistration.register(name, creditCard);
    }

    @Test
    void testAddItemToCart() {
        Cart cart = returned.getCart();
        cart.addItem(childAdultProduct, 1);
        assertEquals(1, cart.getItems().size());
        assertEquals(adultForfait.getPrice() + childForfait.getPrice(), cartHandler.computeCartPrice(cart));
    }
}
