package fr.univcotedazur.skimaster.forfait.dto;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ForfaitOfferRequestDTO(
        @NotNull AgeCategory ageCategory,
        @NotNull PassType passType,
        @Positive double price
) {
}
