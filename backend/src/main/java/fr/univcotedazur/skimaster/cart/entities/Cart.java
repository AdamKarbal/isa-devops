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
    @ManyToMany
    private Set<Product> items = new HashSet<>();

    public Cart(){
    }
    public void addItem(Product p){
        items.add(p);
    }
    public void removeItem(Product p){ items.remove(p);};
    public void setItems(Set<Product> items) {
        this.items = items;
    }
    public Set<Product> getItems(){
        return items;
    }
    public boolean isEmpty(){
        return items.isEmpty();
    }
}
