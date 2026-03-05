package skimaster.entities;

import java.time.Instant;
import java.time.LocalDate;

public class Passage {

    private final Long nfcCardId;
    private final boolean accepted;
    private final LocalDate timestamp;

    public Passage(Long nfcCardId, boolean accepted, LocalDate timestamp) {
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

    public LocalDate getTimestamp() {
        return timestamp;
    }
}
