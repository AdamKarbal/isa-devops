package fr.univcotedazur.skimaster.cart.interfaces;

import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.cart.exceptions.EmptyCartException;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.order.entities.Order;


public interface CartProcessor {
    double computeCartPrice(Cart cart);
    Order validate(Long customerId) throws CustomerIdNotFoundException, EmptyCartException;
}
