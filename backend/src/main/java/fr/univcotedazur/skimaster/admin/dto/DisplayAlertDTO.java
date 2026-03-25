package fr.univcotedazur.skimaster.admin.dto;

import java.time.Instant;

public record DisplayAlertDTO(String message, String gateCode, Instant receivedAt) {
}
