package fr.univcotedazur.skimaster.stats.components;

import fr.univcotedazur.skimaster.forfait.components.ForfaitRegistry;
import fr.univcotedazur.skimaster.gate.components.PassageRegistry;
import fr.univcotedazur.skimaster.gate.components.Portier;
import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProvider;
import org.springframework.stereotype.Component;

import java.nio.file.LinkOption;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    public List<Passage> getDailyPassagesByGate(String gateId) {
        turnstileProxy.syncTodayPassages(gateId);
        return passageRegistry.findTodayPassages(gateId);
    }

    @Override
    public Set<String> getAllGateIds() {
        return turnstileProxy.getAllGateIds();
    }


    @Override
    public List<Passage> getGatePassages(LocalDate day,String gateId) {
        turnstileProxy.syncTodayPassages(gateId);
        return passageRegistry.findByDate(day,gateId);
    }

    @Override
    public long getOpeningTimeByGate(String gateId) {
        return turnstileProxy.syncOpeningTimeByGate(gateId);
    }

    @Override
    public int getTotalUniqueNumberOfPassages() {
        Set<String> allGateIds = getAllGateIds();

        List<Passage> allPassages = allGateIds.stream().flatMap(id->getGatePassages(LocalDate.now(),id).stream()).toList();

        return (int) allPassages.stream()
                .map(Passage::getCustomerId)
                .distinct().count();
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
