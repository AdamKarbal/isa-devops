package fr.univcotedazur.skimaster.product.exceptions;

public class ProductNotFoundException extends RuntimeException {
    private Long id;
    public ProductNotFoundException(Long id) {
        super("Customer not found: " + id);
        this.id = id;
    }
}
