package fr.univcotedazur.skimaster.cart.exceptions;

public class ProductIdNotFound extends RuntimeException {
    public ProductIdNotFound(String message) {
        super(message);
    }
}
