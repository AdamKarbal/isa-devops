package skimaster.dtos;

import fr.univcotedazur.skimaster.incident.entities.Incident;
import skimaster.entities.GateStatus;

import java.util.List;

public record GateStatusResponseDTO(GateStatus status, int threshold, int currentPassages, List<Incident> incidents) {
}
