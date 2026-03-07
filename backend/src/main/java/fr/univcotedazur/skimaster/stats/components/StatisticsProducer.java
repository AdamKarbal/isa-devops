package fr.univcotedazur.skimaster.stats.components;

import fr.univcotedazur.skimaster.forfait.components.ForfaitRegistry;
import fr.univcotedazur.skimaster.gate.components.PassageRegistry;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProducer;
import fr.univcotedazur.skimaster.stats.interfaces.DailyReport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticsProducer implements AnalyticsProducer {
    private final StatisticsProvider statisticsProvider;
    public StatisticsProducer(StatisticsProvider statisticsProvider){
        this.statisticsProvider = statisticsProvider;
    }
    @Override
    public List<DailyReport> aggregateStats() {
        return List.of();
    }
}
