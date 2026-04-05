package fr.univcotedazur.skimaster.gate.connectors.externaldto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record PassageDTO(
    @JsonProperty("nfcCardId") Long nfcCardId, 
    LocalDateTime timestamp,
    String gateId
) {

    public PassageDTO(Long nfcCardId, LocalDateTime timestamp) {
        this(nfcCardId, timestamp, "UNKNOWN");
    }

    public Long forfeitId() {
        return nfcCardId;
    }
    
    public LocalDateTime dateTime() {
        return timestamp;
    }
}