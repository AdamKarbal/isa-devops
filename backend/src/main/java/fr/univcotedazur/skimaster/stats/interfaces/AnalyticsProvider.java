package fr.univcotedazur.skimaster.stats.interfaces;

import fr.univcotedazur.skimaster.gate.entities.Passage;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface AnalyticsProvider {

    List<Passage> getDailyPassages();
    List<Passage> getPassagesByDay(LocalDate day);
    Date getOpeningTimeByGate(String gateId);
    int getTotalUniqueNumberOfPassages();
}
