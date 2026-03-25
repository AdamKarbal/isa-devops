package skimaster.dtos;

import skimaster.entities.GateStatus;
import skimaster.entities.Incident;

import java.util.List;

public record GateStatusResponseDTO(GateStatus status, int alertThreshold, int currentPassages, List<Incident> incidents) {
}
