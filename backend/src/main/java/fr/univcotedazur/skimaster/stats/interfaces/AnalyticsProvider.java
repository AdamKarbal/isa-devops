package fr.univcotedazur.skimaster.stats.interfaces;

import fr.univcotedazur.skimaster.gate.entities.Passage;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface AnalyticsProvider {

    List<Passage> getDailyPassagesByGate(String gateId);
    List<Passage> getGatePassages(LocalDate day,String gateId);
    long getOpeningTimeByGate(String gateId);
    int getTotalUniqueNumberOfPassages();
    int getNbActiveForfaitsForDay(LocalDate date);
    Set<String> getAllGateIds();
}
