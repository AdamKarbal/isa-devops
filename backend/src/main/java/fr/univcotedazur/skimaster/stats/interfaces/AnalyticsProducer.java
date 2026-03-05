package fr.univcotedazur.skimaster.stats.interfaces;

import java.util.List;

public interface AnalyticsProducer {
    List<DailyReport> aggregateStats();
}
