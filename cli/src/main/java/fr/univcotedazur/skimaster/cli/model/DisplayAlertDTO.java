package fr.univcotedazur.skimaster.cli.model;

import java.time.Instant;

public record DisplayAlertDTO(String message, String gateCode, Instant receivedAt) {
}
