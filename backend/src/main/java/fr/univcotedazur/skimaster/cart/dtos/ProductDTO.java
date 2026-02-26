package fr.univcotedazur.skimaster.cart.dtos;

import java.util.List;

public record ProductDTO(Long id,
                         List<Long> forfaitOfferIds,
                         int reduction) {
}
