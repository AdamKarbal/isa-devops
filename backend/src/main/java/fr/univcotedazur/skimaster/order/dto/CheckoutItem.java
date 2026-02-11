package fr.univcotedazur.skimaster.order.dto;

public record CheckoutItem(
        Long productId,
        int quantity
) {}
