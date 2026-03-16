package fr.univcotedazur.skimaster.stats.components;

import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.stats.dtos.DailyReportDTO;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProducer;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class StatisticsProducer implements AnalyticsProducer {
    private final AnalyticsProvider analyticsProvider;
    public StatisticsProducer(AnalyticsProvider statisticsProvider){
        this.analyticsProvider = statisticsProvider;
    }

    public DailyReportDTO aggregateTodayStats() {
        return aggregateStatsByDay(LocalDate.now());
    }

    public DailyReportDTO aggregateStatsByDay(LocalDate day){
        List<Passage> passages = analyticsProvider.getPassagesByDay(day);
        int totalPassages = passages.size();
        int uniqueSkiers = analyticsProvider.getTotalUniqueNumberOfPassages();
        int activeForfaits = analyticsProvider.getNbActiveForfaitsForDay(day);

        return new DailyReportDTO(day, totalPassages, uniqueSkiers, activeForfaits);
    }
}
