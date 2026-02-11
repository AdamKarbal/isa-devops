package fr.univcotedazur.skimaster.forfait.dto;

import java.time.LocalDate;

public record ForfaitDTO(
        Long id,
        Long customerId,
        String passType,
        String ageCategory,
        LocalDate validDate
) {}
