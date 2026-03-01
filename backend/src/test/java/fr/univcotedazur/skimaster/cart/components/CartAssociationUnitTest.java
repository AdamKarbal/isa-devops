package fr.univcotedazur.skimaster.cart.components;

import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.enums.OrderStatus;
import fr.univcotedazur.skimaster.order.factories.OrderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class CartAssociationUnitTest {
    @Test
    void cartProductForfaitOfferAssociationShouldBeConsistent() {
        ForfeitOffer adultWeekly = new ForfeitOffer(AgeCategory.ADULT, PassType.WEEKLY, 250.0);
        Product product = new Product(List.of(adultWeekly), 10);
        Cart cart = new Cart();

        cart.addItem(product,1);

        Assertions.assertEquals(1, cart.getItems().size());
        Product cartItem = cart.getProducts().iterator().next();
        Assertions.assertEquals(1, cartItem.getItems().size());
        Assertions.assertEquals(PassType.WEEKLY, cartItem.getItems().getFirst().getPassType());
        Assertions.assertEquals(AgeCategory.ADULT, cartItem.getItems().getFirst().getAgeCategory());
    }

    @Test
    void cartUsesSetSoSameProductTwiceKeepsSingleCartItem() {
        ForfeitOffer childDaily = new ForfeitOffer(AgeCategory.CHILD, PassType.FULL, 35.0);
        Product product = new Product(List.of(childDaily), 5);
        Cart cart = new Cart();

        cart.addItem(product,1);
        cart.addItem(product,1);

        Assertions.assertEquals(1, cart.getItems().size());
    }

    @Test
    void forfaitShouldDelegateAgeAndPassTypeToOffer() {
        ForfeitOffer adultSeason = new ForfeitOffer(AgeCategory.ADULT, PassType.SEASONAL, 499.0);
        Forfait forfait = new Forfait(42L, LocalDate.now().plusDays(7), adultSeason);

        Assertions.assertEquals(AgeCategory.ADULT, forfait.getAgeCategory());
        Assertions.assertEquals(PassType.SEASONAL, forfait.getPassType());
    }

    @Test
    void orderFactoryShouldKeepCustomerAndCartItemsAssociations() throws Exception {
        CartHandler cartHandler = Mockito.mock(CartHandler.class);
        OrderFactory orderFactory = new OrderFactory(cartHandler);

        ForfeitOffer adultDaily = new ForfeitOffer(AgeCategory.ADULT, PassType.FULL, 50.0);
        Product product = new Product(List.of(adultDaily), 1);

        Customer customer = new Customer("john", "1234567890");
        customer.getCart().addItem(product,1);

        Mockito.when(cartHandler.computeCartPrice(customer.getCart())).thenReturn(50.0);

        Order order = orderFactory.create(customer);

        Assertions.assertEquals(customer, order.getCustomer());
        Assertions.assertTrue(customer.getOrders().contains(order));

        Set<Product> orderItems = order.getItems();
        double orderPrice = order.getPrice();

        Assertions.assertEquals(1, orderItems.size());
        Assertions.assertTrue(orderItems.contains(product));
        Assertions.assertEquals(50.0, orderPrice);
    }
}
