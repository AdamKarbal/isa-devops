package fr.univcotedazur.skimaster.order.factories;
import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.cart.components.CartHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class OrderFactory {

    private final CartHandler cartHandler;

    public OrderFactory(@Lazy CartHandler cartHandler) {
        this.cartHandler = cartHandler;
    }

    public Order create(Customer customer) {
        Cart cart = customer.getCart();
        double price = cartHandler.computeCartPrice(cart);
        Order order = new Order(customer,cart.getItems(),price);
        return order;
    }
}

