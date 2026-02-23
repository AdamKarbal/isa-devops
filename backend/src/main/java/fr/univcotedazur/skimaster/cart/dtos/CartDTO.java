package fr.univcotedazur.skimaster.cart.dtos;

import fr.univcotedazur.skimaster.cart.entities.Product;

import java.util.Set;

public record CartDTO(Set<Product> items) {
}
