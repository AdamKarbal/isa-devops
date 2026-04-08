package fr.univcotedazur.skimaster.cli.model;

import java.time.LocalDateTime;

public record CliStatsPassage(Long customerId, LocalDateTime occurrenceDate) {
}
