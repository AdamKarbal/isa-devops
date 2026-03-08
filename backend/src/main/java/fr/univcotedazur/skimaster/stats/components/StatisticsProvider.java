package fr.univcotedazur.skimaster.stats.components;

import fr.univcotedazur.skimaster.forfait.components.ForfaitRegistry;
import fr.univcotedazur.skimaster.gate.components.PassageRegistry;
import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Component
public class StatisticsProvider implements AnalyticsProvider {
    private final ForfaitRegistry forfaitRegistry;
    private final PassageRegistry passageRegistry;
    public StatisticsProvider(ForfaitRegistry forfaitRegistry,PassageRegistry passageRegistry){
        this.forfaitRegistry=forfaitRegistry;
        this.passageRegistry = passageRegistry;
    }
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
    @Override
    public int getNbActiveForfaitsForDay(LocalDate day){
        return (int) forfaitRegistry.findAllForfaits().stream()
                .filter(forfait -> !forfait.getValidDate().isBefore(day))
                .count();
    }
    public int todayActiveForfaits(){
        return getNbActiveForfaitsForDay(LocalDate.now());
    }
}
