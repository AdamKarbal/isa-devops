package skimaster.entities;

import java.time.Instant;

public class Passage {

    private final String nfcCardId;
    private final boolean accepted;
    private final Instant timestamp;

    public Passage(String nfcCardId, boolean accepted, Instant timestamp) {
        this.nfcCardId = nfcCardId;
        this.accepted = accepted;
        this.timestamp = timestamp;
    }

    public String getNfcCardId() {
        return nfcCardId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
