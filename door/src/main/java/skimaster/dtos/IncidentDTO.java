package skimaster.dtos;

import skimaster.entities.IncidentType;

import java.time.Instant;

public record IncidentDTO(IncidentType type, String description, Instant timestamp) {
}
