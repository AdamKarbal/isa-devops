package skimaster.dtos;

import java.time.Instant;
import java.time.LocalDate;

public record PassageDTO(Long nfcCardId, LocalDate timestamp) {
}
