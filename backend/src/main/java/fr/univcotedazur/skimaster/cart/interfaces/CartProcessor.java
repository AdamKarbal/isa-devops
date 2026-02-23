package fr.univcotedazur.skimaster.cart.interfaces;

import fr.univcotedazur.skimaster.cart.entities.Cart;
import org.hibernate.query.Order;

public interface CartProcessor {
    double computeCartPrice(Cart cart);
    Order validate(Cart cart);
}
