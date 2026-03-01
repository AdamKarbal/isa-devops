package fr.univcotedazur.skimaster.gate.dtos;

import fr.univcotedazur.skimaster.gate.enums.BeepSignal;

public record AccessRequest(
        Long forfeitId,
        BeepSignal beepSignal,
        boolean isAuthorized
) {
}
