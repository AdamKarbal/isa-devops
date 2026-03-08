package fr.univcotedazur.skimaster.stats.dtos;

import java.time.LocalDate;

public record DailyReportDTO(LocalDate date, int totalPassages, int uniqueSkiers, int activeForfaits) {
}
