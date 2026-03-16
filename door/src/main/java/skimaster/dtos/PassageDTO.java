package skimaster.dtos;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PassageDTO(Long nfcCardId, LocalDateTime timestamp,String gateId) {
}
