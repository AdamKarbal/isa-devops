package fr.univcotedazur.skimaster.stats.components;

import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProvider;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class StatisticsProvider implements AnalyticsProvider {
    @Override
    public List<Passage> getDailyPassages() {
        return List.of();
    }

    @Override
    public List<Passage> getPassagesByDay(LocalDate day) {
        return List.of();
    }

    @Override
    public Date getOpeningTimeByGate(String gateId) {
        return null;
    }

    @Override
    public int getTotalUniqueNumberOfPassages() {
        return 0;
    }
}
