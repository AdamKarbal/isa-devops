package fr.univcotedazur.skimaster.cli.model;

import java.time.LocalDate;

public record CliDailyReport(LocalDate date, int totalPassages, int uniqueSkiers, int activeForfaits) {
}
