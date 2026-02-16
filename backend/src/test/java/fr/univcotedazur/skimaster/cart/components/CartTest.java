package fr.univcotedazur.skimaster.cart.components;

import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerRegistration;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.ForfaitOffer;
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
    private final ForfaitOffer adultForfait = new ForfaitOffer(AgeCategory.ADULT, PassType.FULL,10);
    private final ForfaitOffer childForfait = new ForfaitOffer(AgeCategory.CHILD,PassType.FULL,5);
    private final Product childAdultProduct = new Product(List.of(adultForfait,childForfait));

    @BeforeEach
    void setup() throws AlreadyExistingCustomerException {
        returned = customerRegistration.register(name,creditCard);
    }

    @Test
    void basicAddCart(){
        Cart cart = returned.getCart();
        cart.addItem(childAdultProduct,1);
        assertEquals(cart.getItems().size(),1);
        assertEquals(cartHandler.computeCartPrice(cart),adultForfait.getPrice()+childForfait.getPrice());
    }




}
