package fr.univcotedazur.skimaster.stats.components;

import fr.univcotedazur.skimaster.forfait.components.ForfaitRegistry;
import fr.univcotedazur.skimaster.gate.components.PassageRegistry;
import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
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
    private final TurnstileProxy turnstileProxy;
    public StatisticsProvider(ForfaitRegistry forfaitRegistry,PassageRegistry passageRegistry,TurnstileProxy turnstileProxy) {
        this.forfaitRegistry=forfaitRegistry;
        this.passageRegistry = passageRegistry;
        this.turnstileProxy = turnstileProxy;
    }
    @Override
    public List<Passage> getDailyPassages() {
        return passageRegistry.findTodayPassages();
    }

    @Override
    public List<Passage> getPassagesByDay(LocalDate day) {
        return passageRegistry.findByDate(day);
    }

    @Override
    public long getOpeningTimeByGate(String gateId) {
        return turnstileProxy.syncOpeningTimeByGate(gateId);
    }

    @Override
    public int getTotalUniqueNumberOfPassages() {
        return (int) passageRegistry.findTodayPassages().stream().distinct().count();
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
