package fr.univcotedazur.skimaster.stats.interfaces;

import fr.univcotedazur.skimaster.stats.dtos.DailyReportDTO;

import java.util.List;

public interface AnalyticsProducer {
    DailyReportDTO aggregateTodayStats(String gateId);
}
