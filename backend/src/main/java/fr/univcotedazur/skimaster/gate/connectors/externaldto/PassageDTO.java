package fr.univcotedazur.skimaster.gate.connectors.externaldto;

import java.time.LocalDateTime;

public record PassageDTO(Long nfcCardId,
                         LocalDateTime timestamp,
                         String gateId) {
}
