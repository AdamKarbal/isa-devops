package skimaster.dtos;

import skimaster.entities.GateStatus;

public record PassageResponseDTO(boolean accepted, GateStatus gateStatus) {
}
