package fr.univcotedazur.skimaster.cli.model;

public record GateMonitoringStatusDTO(String gateId, String status, int alertThreshold) {
}
