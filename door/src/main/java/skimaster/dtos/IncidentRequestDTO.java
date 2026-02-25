package skimaster.dtos;

import skimaster.entities.IncidentType;

public record IncidentRequestDTO(IncidentType incidentType,String description) {
}
