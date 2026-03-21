package fr.univcotedazur.skimaster.stats.components;

import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.stats.dtos.DailyReportDTO;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProducer;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProvider;
import org.springframework.stereotype.Component;

import java.time.Duration;
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

    public String aggregateOpeningTimeByGate(String gateId){
        long time = analyticsProvider.getOpeningTimeByGate(gateId);
        Duration duration = Duration.ofSeconds(time);

        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long seconds = duration.minusMinutes(minutes).toSeconds();

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);

    }
}
