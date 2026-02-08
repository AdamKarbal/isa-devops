package fr.univcotedazur.skimaster.customer.exceptions;

public class CustomerIdNotFoundException extends Exception {

    private Long id;

    public CustomerIdNotFoundException(Long id) {
        super("Customer not found: " + id);
        this.id = id;
    }

    public CustomerIdNotFoundException() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
