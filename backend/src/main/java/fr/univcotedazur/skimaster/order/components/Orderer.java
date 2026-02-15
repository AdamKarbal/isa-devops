package fr.univcotedazur.skimaster.order.components;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.factories.OrderFactory;
import fr.univcotedazur.skimaster.order.interfaces.OrderCreator;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class Orderer implements OrderCreator {

    private final OrderFactory orderFactory;
    private final OrderRepository orderRepository;

    public Orderer(OrderFactory orderFactory,OrderRepository orderRepository) {
        this.orderFactory = orderFactory;
        this.orderRepository=orderRepository;
    }

    public Order createOrder(Customer customer) {
        Order order = orderFactory.create(customer);
        return orderRepository.save(order);
    }
}
