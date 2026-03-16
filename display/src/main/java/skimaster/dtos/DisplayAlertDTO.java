package skimaster.dtos;

import java.time.Instant;

public record DisplayAlertDTO(String message, String gateCode, Instant receivedAt) {
}
