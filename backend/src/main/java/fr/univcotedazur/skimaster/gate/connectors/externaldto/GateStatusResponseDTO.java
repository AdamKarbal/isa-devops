package fr.univcotedazur.skimaster.gate.connectors.externaldto;

public record GateStatusResponseDTO(String gateId, String status, int alertThreshold) {
}
