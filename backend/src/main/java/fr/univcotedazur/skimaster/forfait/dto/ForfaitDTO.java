package fr.univcotedazur.skimaster.forfait.dto;

public record ForfaitDTO(
        Long id,
        Long userId,
        String passType,
        String ageCategory,
        java.time.@jakarta.validation.constraints.NotNull LocalDate validUntil
) {}
