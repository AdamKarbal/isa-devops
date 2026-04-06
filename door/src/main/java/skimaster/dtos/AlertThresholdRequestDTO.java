package skimaster.dtos;

import jakarta.validation.constraints.Min;

public record AlertThresholdRequestDTO(
        @Min(value = 0, message = "La jauge d'alerte doit etre positive ou nulle.")
        int alertThreshold
) {
}
