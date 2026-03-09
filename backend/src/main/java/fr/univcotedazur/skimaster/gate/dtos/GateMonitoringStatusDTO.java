package fr.univcotedazur.skimaster.gate.dtos;

public record GateMonitoringStatusDTO(
        String gateId,
        String status,
        int alertThreshold
) {
}
