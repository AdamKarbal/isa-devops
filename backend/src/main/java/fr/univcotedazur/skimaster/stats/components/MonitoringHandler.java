package fr.univcotedazur.skimaster.stats.components;

import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProducer;
import fr.univcotedazur.skimaster.stats.interfaces.DailyReport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MonitoringHandler implements AnalyticsProducer {
    @Override
    public List<DailyReport> aggregateStats() {
        return List.of();
    }
}
