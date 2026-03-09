package fr.univcotedazur.skimaster.gate.dtos;

import jakarta.validation.constraints.Min;

public record GateAlertThresholdRequestDTO(
        @Min(value = 0, message = "La jauge d'alerte doit etre positive ou nulle.")
        int alertThreshold
) {
}
