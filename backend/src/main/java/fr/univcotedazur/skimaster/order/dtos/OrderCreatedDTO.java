package fr.univcotedazur.skimaster.order.dtos;

import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.order.enums.OrderStatus;

import java.util.Set;

public record OrderCreatedDTO(Long orderId, Set<Product> items, double price, OrderStatus status) {
}
