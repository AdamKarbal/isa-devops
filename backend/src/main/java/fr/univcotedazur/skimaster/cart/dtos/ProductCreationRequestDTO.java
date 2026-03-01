package fr.univcotedazur.skimaster.cart.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProductCreationRequestDTO(
        @NotEmpty List<@NotNull Long> forfaitOfferIds,
        @Min(1) @Max(100) int reduction
) {
}
