package fr.univcotedazur.skimaster.cart.interfaces;

import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cart.exceptions.ProductIdNotFound;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;

public interface CartModifier { // penser à généraliser
    void addItem(Long customerId, Long productId,int quantity) throws CustomerIdNotFoundException, ProductIdNotFound;
    void removeItem(Long customerId,Long productId,int quantity) throws CustomerIdNotFoundException, ProductIdNotFound;
}
