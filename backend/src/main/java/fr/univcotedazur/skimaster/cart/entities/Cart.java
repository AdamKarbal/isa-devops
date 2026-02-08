package fr.univcotedazur.skimaster.cart.entities;

import jakarta.persistence.Entity;
import org.springframework.http.server.reactive.AbstractListenerWriteFlushProcessor;

import java.util.ArrayList;
import java.util.List;
@Entity
public class Cart {

    List<Product> items;
    public Cart(){
        items = new ArrayList<>();
    }
    void addItem(Product p){
        items.add(p);
    }
}
