package fr.univcotedazur.skimaster.cart.components;

import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerRegistration;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import jakarta.transaction.Transactional;
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
    private Customer returned;

    private final String name = "John";
    private final String creditCard = "1234567890";
    private final ForfeitOffer adultForfait = new ForfeitOffer(AgeCategory.ADULT, PassType.FULL,10);
    private final ForfeitOffer childForfait = new ForfeitOffer(AgeCategory.CHILD,PassType.FULL,5);
    private final Product childAdultProduct = new Product(List.of(adultForfait,childForfait));

    @BeforeEach
    void setup() throws AlreadyExistingCustomerException {
        returned = customerRegistration.register(name,creditCard);
    }

    @Test
    void basicAddCart(){
        Cart cart = returned.getCart();
        cart.addItem(childAdultProduct, 1);
        assertEquals(1, cart.getItems().size());
        double actualPrice = cartHandler.computeCartPrice(cart);
        assertEquals(14.85, actualPrice, 0.01);
    }



}
