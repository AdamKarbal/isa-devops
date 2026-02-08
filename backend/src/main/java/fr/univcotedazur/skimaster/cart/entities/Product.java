package fr.univcotedazur.skimaster.cart.entities;

import fr.univcotedazur.skimaster.cart.interfaces.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;
@Entity
public class Product {
    @ManyToMany
    private List<Item> packageItems = new ArrayList<>();
    @Id
    @GeneratedValue
    long productId;
    int price;
    public Product(){
    }
    public void addItem(Item item) {
        packageItems.add(item);
    }

    public void removeItem(Item item) {
        packageItems.remove(item);
    }

    public List<Item> getItems() { return packageItems; }
    public Long getId() { return productId; }
}
