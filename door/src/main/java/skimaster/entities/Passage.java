package skimaster.entities;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Passage {

    private final Long nfcCardId;
    private final boolean accepted;
    private final LocalDateTime timestamp;

    public Passage(Long nfcCardId, boolean accepted, LocalDateTime timestamp) {
        this.nfcCardId = nfcCardId;
        this.accepted = accepted;
        this.timestamp = timestamp;
    }

    public Long getNfcCardId() {
        return nfcCardId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
