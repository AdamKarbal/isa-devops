package fr.univcotedazur.skimaster.cli.model;

import java.util.List;

public record GateSyncDTO(List<Long> authorizedCardIds) {
}
