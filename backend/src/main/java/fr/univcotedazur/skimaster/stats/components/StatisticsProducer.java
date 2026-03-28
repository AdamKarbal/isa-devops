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

    public DailyReportDTO aggregateTodayStats(String gateId) {
        return aggregateStatsByDay(LocalDate.now(),gateId);
    }

    public DailyReportDTO aggregateStatsByDay(LocalDate day,String gateId){
        List<Passage> passages = analyticsProvider.getGatePassages(day,gateId);
        int totalPassages = passages.size();
        int uniqueSkiers = (int) passages.stream()
                .map(Passage::getCustomerId)
                .distinct()
                .count();
        int activeForfaits = analyticsProvider.getNbActiveForfaitsForDay(day);

        return new DailyReportDTO(day, totalPassages, uniqueSkiers, activeForfaits);
    }

    public String aggregateOpeningTimeByGate(String gateId){
        long time = analyticsProvider.getOpeningTimeByGate(gateId);
        Duration duration = Duration.ofSeconds(time);

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);

    }
}
