package fr.univcotedazur.skimaster.cart.interfaces;

import jakarta.persistence.Embeddable;

@Embeddable
public interface Item {
    double getPrice();
    long getId();
}
