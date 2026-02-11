package fr.univcotedazur.skimaster.order.dto;

import fr.univcotedazur.skimaster.order.OrderStatus;

import java.util.List;

public record OrderDTO(
        Long id,
        OrderStatus status,
        int total,
        List<OrderLineDTO> lines
) {}
