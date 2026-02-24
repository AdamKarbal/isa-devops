package fr.univcotedazur.skimaster.order.components;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.factories.OrderFactory;
import fr.univcotedazur.skimaster.order.interfaces.OrderCreator;
import org.springframework.stereotype.Component;

@Component
public class Orderer implements OrderCreator {

    private final OrderFactory orderFactory;

    public Orderer(OrderFactory orderFactory) {
        this.orderFactory = orderFactory;
    }

    public Order createOrder(Customer customer) {
        return orderFactory.create(customer);
    }
}
