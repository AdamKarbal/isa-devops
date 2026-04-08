package fr.univcotedazur.skimaster.forfait.dto;

import fr.univcotedazur.skimaster.gate.enums.BeepSignal;

import java.time.LocalDate;

public record ForfaitDTO(
        Long id,
        Long customerId,
        String passType,
        String ageCategory,
        LocalDate validDate,
        BeepSignal beep
) {}
