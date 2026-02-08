package fr.univcotedazur.skimaster.forfait.dto;

import java.util.Date;

public record ForfaitDTO(
        Long id,
        Long userId,
        String passType,
        String ageCategory,
        Date validUntil
) {}
