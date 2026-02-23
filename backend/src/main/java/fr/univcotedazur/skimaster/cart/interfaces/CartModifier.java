package fr.univcotedazur.skimaster.cart.interfaces;

import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;

public interface CartModifier { // penser à généraliser
    void addItem(Long customerId, Product p,int quantity) throws CustomerIdNotFoundException;
    void removeItem(Long customerId,Product p,int quantity);
}
