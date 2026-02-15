package fr.univcotedazur.skimaster.cart.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Cart {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public Cart() {}

    public Long getId() { return id; }
    public List<CartItem> getItems() { return items; }
    public Set<Product> getProducts(){
        Set<Product> products = new HashSet<>();
        for(CartItem item : items){
            products.add(item.getProduct());
        }
        return products;
    }

    public boolean isEmpty(){
        return items.isEmpty();
    }

    public void addItem(Product p, int quantity){
        for (CartItem item : items) {
            if (item.getProduct().equals(p)) {
                item.addQuantity(quantity);
                return;
            }
        }
        items.add(new CartItem(this, p, quantity));
    }

    public void removeItem(Product p, int quantity) {
        CartItem target = null;

        for (CartItem item : items) {
            if (item.getProduct().equals(p)) {
                target = item;
                break;
            }
        }

        if (target == null) {
            throw new IllegalArgumentException("Product not present in cart");
        }

        target.removeQuantity(quantity);

        if (target.getQuantity() <= 0) {
            items.remove(target);
        }
    }
}
