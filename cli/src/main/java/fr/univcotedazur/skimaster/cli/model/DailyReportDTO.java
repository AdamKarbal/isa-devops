package fr.univcotedazur.skimaster.cli.model;

import java.time.LocalDate;

public record DailyReportDTO(LocalDate date, int totalPassages, int uniqueSkiers, int activeForfaits) {
}
