package fr.univcotedazur.skimaster.order.dtos;

import fr.univcotedazur.skimaster.cart.entities.Product;

import java.util.List;
import java.util.Set;

public record OrderCreatedDTO(Long orderId, Set<Product> items, double price) {}
