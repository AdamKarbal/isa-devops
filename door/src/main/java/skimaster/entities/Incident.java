package skimaster.entities;

import java.time.Instant;

public class Incident {

    private final IncidentType type;
    private final String description;
    private final Instant timestamp;

    public Incident(IncidentType type, String description, Instant timestamp) {
        this.type = type;
        this.description = description;
        this.timestamp = timestamp;
    }

    public IncidentType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
