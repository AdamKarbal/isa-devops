package skimaster.dtos;

import java.time.Instant;

public record PassageDTO(Long nfcCardId, boolean accepted, Instant timestamp) {
}
