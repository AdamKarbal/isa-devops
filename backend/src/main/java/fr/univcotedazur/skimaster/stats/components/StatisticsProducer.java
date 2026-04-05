package fr.univcotedazur.skimaster.stats.components;

import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.stats.dtos.DailyReportDTO;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProducer;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProvider;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
public class StatisticsProducer implements AnalyticsProducer {
    private final AnalyticsProvider analyticsProvider;
    public StatisticsProducer(AnalyticsProvider statisticsProvider){
        this.analyticsProvider = statisticsProvider;
    }

    public DailyReportDTO aggregateTodayStats(String gateId) {
        return aggregateStatsByDay(LocalDate.now(),gateId);
    }

    public DailyReportDTO aggregateGlobalStats(LocalDate day){

        Set<String> allGateIds = analyticsProvider.getAllGateIds();

        List<Passage> allPassages = allGateIds.stream().flatMap(id->analyticsProvider.getGatePassages(day,id).stream()).toList();
        int totalPassages = allPassages.size();

        int uniqueSkiers = (int) allPassages.stream()
                .map(Passage::getCustomerId)
                .distinct().count();

         long totalSeconds = allGateIds.stream().mapToLong(analyticsProvider::getOpeningTimeByGate).filter(time -> time != -1L).sum();
         String formattedTime = formatDuration(totalSeconds);
        return new DailyReportDTO(day,totalPassages,uniqueSkiers,formattedTime);
    }

    public DailyReportDTO aggregateStatsByDay(LocalDate day,String gateId){
        List<Passage> passages = analyticsProvider.getGatePassages(day,gateId);
        int totalPassages = passages.size();
        int uniqueSkiers = (int) passages.stream()
                .map(Passage::getCustomerId)
                .distinct()
                .count();
        String openingTime = aggregateOpeningTimeByGate(gateId);

        return new DailyReportDTO(day, totalPassages, uniqueSkiers, openingTime);
    }

    public String aggregateOpeningTimeByGate(String gateId){
        long time = analyticsProvider.getOpeningTimeByGate(gateId);
        return formatDuration(time);

    }

    private String formatDuration(long seconds) {
        Duration duration = Duration.ofSeconds(seconds);
        return String.format("%02d:%02d:%02d",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart());
    }
}
