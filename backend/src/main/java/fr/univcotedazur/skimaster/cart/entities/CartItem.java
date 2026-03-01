package fr.univcotedazur.skimaster.cart.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
public class CartItem {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Product product;

    @Min(1)
    private int quantity;

    protected CartItem() {}

    public CartItem(Cart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }

    public void addQuantity(int q) {
        this.quantity += q;
    }

    public void removeQuantity(int q) {
        if (q > quantity) {
            throw new IllegalArgumentException("Cannot remove " + q + ", only " + quantity);
        }
        this.quantity -= q;
    }
}
