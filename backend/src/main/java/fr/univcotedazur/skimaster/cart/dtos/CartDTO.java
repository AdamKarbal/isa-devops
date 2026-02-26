package fr.univcotedazur.skimaster.cart.dtos;

import fr.univcotedazur.skimaster.cart.entities.CartItem;

import java.util.List;
import java.util.Set;

public record CartDTO(List<CartItem> items) {
}
