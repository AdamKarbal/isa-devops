package fr.univcotedazur.skimaster.cart.components;

import fr.univcotedazur.skimaster.cart.dtos.CartDTO;
import fr.univcotedazur.skimaster.cart.dtos.CartItemRequestDTO;
import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.cart.entities.CartItem;
import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cart.exceptions.EmptyCartException;
import fr.univcotedazur.skimaster.cart.exceptions.ForfaitOfferIdNotFound;
import fr.univcotedazur.skimaster.cart.repositories.ProductRepository;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.cart.exceptions.ProductIdNotFound;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerRegistration;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.repositories.ForfeitOfferRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CartTest {
    @Autowired
    private CustomerRegistration customerRegistration;
    @Autowired
    private CartHandler cartHandler;
    private Customer returned;

    @Autowired
    private ForfeitOfferRepository forfeitOfferRepository;

    @Autowired
    private ProductCatalogHandler productCatalogHandler;

    @Autowired
    private ProductRepository productRepository;

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

    @Test
    void validate_throwsWhenCustomerNotFound() throws CustomerIdNotFoundException {
        assertThrows(CustomerIdNotFoundException.class,
                () -> cartHandler.validate(999L));
    }

    @Test
    void validate_throwsWhenCartIsEmpty() throws CustomerIdNotFoundException {
        assertThrows(EmptyCartException.class,
                () -> cartHandler.validate(returned.getId()));
    }

    @Test
    void addItem_throwsWhenQuantityNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> cartHandler.addItem(1L, 2L, -1));
    }

    @Test
    void addItem_throwsWhenProductNotFound() throws CustomerIdNotFoundException {
        assertThrows(ProductIdNotFound.class,
                () -> cartHandler.addItem(returned.getId(), 999L, 1));
    }

    @Test
    void removeItem_throwsWhenQuantityNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> cartHandler.removeItem(returned.getId(), 999L, -1));
    }


    @Test
    void findForfaitOffer_returnsOfferWhenFound() {
        ForfeitOffer offer = new ForfeitOffer(AgeCategory.ADULT, PassType.DAILY, 20);
        forfeitOfferRepository.save(offer);

        Optional<ForfeitOffer> result = productCatalogHandler.findForfaitOffer(AgeCategory.ADULT, PassType.DAILY);

        assertTrue(result.isPresent());
        assertEquals(offer, result.get());
    }

    @Test
    void findForfaitOffer_returnsEmptyWhenNotFound() {
        Optional<ForfeitOffer> result = productCatalogHandler.findForfaitOffer(AgeCategory.ADULT, PassType.HALFADAY);

        assertTrue(result.isEmpty());
    }

    @Test
    void createProduct_throwsWhenOfferNotFound() {
        assertThrows(ForfaitOfferIdNotFound.class,
                () -> productCatalogHandler.createProduct(List.of(999L), 10));
    }

    @Test
    void retrieveProduct_throwsWhenNotFound() {
        assertThrows(ProductIdNotFound.class,
                () -> productCatalogHandler.retrieveProduct(999L));
    }

    @Test
    void dtoStoresItemsCorrectly() {
        ForfeitOffer offer1 = forfeitOfferRepository.save(new ForfeitOffer(AgeCategory.ADULT, PassType.DAILY, 20));
        ForfeitOffer offer2 = forfeitOfferRepository.save(new ForfeitOffer(AgeCategory.CHILD, PassType.FULL, 10));

        Product product1 = productRepository.save(new Product(List.of(offer1)));
        Product product2 = productRepository.save(new Product(List.of(offer2)));

        CartItem item1 = new CartItem(null, product1,2);
        CartItem item2 = new CartItem(null, product2,2);

        CartDTO dto = new CartDTO(List.of(item1, item2));

        assertEquals(2, dto.items().size());
        assertEquals(item1, dto.items().get(0));
        assertEquals(item2, dto.items().get(1));
    }

    @Test
    void dtoEqualityWorks() {
        ForfeitOffer offer1 = forfeitOfferRepository.save(new ForfeitOffer(AgeCategory.ADULT, PassType.DAILY, 20));
        Product product1 = productRepository.save(new Product(List.of(offer1)));
        CartItem item = new CartItem(null, product1,2);

        CartDTO a = new CartDTO(List.of(item));
        CartDTO b = new CartDTO(List.of(item));

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());


        CartItemRequestDTO c = new CartItemRequestDTO(product1.getId(), 3);
        CartItemRequestDTO d = new CartItemRequestDTO(product1.getId(), 3);

        assertEquals(c, d);
        assertEquals(c.hashCode(), d.hashCode());
    }

    @Test
    void dtoInequalityWorks() {
        ForfeitOffer offer1 = forfeitOfferRepository.save(new ForfeitOffer(AgeCategory.ADULT, PassType.DAILY, 20));
        ForfeitOffer offer2 = forfeitOfferRepository.save(new ForfeitOffer(AgeCategory.CHILD, PassType.FULL, 10));

        Product product1 = productRepository.save(new Product(List.of(offer1)));
        Product product2 = productRepository.save(new Product(List.of(offer2)));

        CartItem item1 = new CartItem(null, product1,2);
        CartItem item2 = new CartItem(null, product2,2);

        CartDTO a = new CartDTO(List.of(item1));
        CartDTO b = new CartDTO(List.of(item2));

        assertNotEquals(a, b);


        CartItemRequestDTO c = new CartItemRequestDTO(product1.getId(), 3);
        CartItemRequestDTO d = new CartItemRequestDTO(product2.getId(), 3);

        assertNotEquals(c, d);
    }

    @Test
    void dtoIsImmutable() {
        ForfeitOffer offer1 = forfeitOfferRepository.save(new ForfeitOffer(AgeCategory.ADULT, PassType.DAILY, 20));
        Product product1 = productRepository.save(new Product(List.of(offer1)));
        CartItem item = new CartItem(null, product1, 2);
        CartDTO dto = new CartDTO(List.of(item));

        // Records have no setters
        assertThrows(NoSuchMethodException.class,
                () -> CartDTO.class.getDeclaredMethod("setItems", List.class));

        CartItemRequestDTO itemDTO = new CartItemRequestDTO(product1.getId(), 3);

        // Records have no setters
        assertThrows(NoSuchMethodException.class,
                () -> CartItemRequestDTO.class.getDeclaredMethod("setProductId", Long.class));
    }


    @Test
    void dtoStoresValuesCorrectly() {
        CartItemRequestDTO dto = new CartItemRequestDTO(42L, 3);

        assertEquals(42L, dto.productId());
        assertEquals(3, dto.quantity());
    }
}
