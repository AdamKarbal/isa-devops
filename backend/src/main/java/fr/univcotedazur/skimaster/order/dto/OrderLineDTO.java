package fr.univcotedazur.skimaster.order.dto;

public record OrderLineDTO(
        int quantity,
        int unitPrice,
        int lineTotal
) {}
