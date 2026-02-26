package fr.univcotedazur.skimaster.forfait.dto;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.PassType;

public record ForfaitOfferDTO(
        Long id,
        AgeCategory ageCategory,
        PassType passType,
        double price
) {
}
