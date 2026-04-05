package fr.univcotedazur.skimaster.cucumber.stats;

import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.gate.exceptions.NoGateExistentException;
import fr.univcotedazur.skimaster.gate.repositories.PassageRepository;
import fr.univcotedazur.skimaster.stats.dtos.DailyReportDTO;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProducer;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProvider;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Transactional
public class GeneratingDailyReport {

    @Autowired
    private PassageRepository passageRepository;

    @Autowired
    private TurnstileProxy turnstileProxy;

    @Autowired
    private AnalyticsProvider analyticsProvider;

    @Autowired
    private AnalyticsProducer analyticsProducer;

    private DailyReportDTO report;

    @Before
    public void settingUpContext() {
        passageRepository.deleteAll();
    }

    @Given("a gate named {string} exists")
    public void aGateNamedExists(String gateId) {
        when(turnstileProxy.getAllGateIds()).thenReturn(Set.of(gateId));
    }

    @And("the gate {string} was open for {long} seconds")
    public void theGateWasOpenForSeconds(String gateId, long seconds) {
        when(turnstileProxy.syncOpeningTimeByGate(gateId)).thenReturn(seconds);
    }

    @And("{int} unique skiers passed through {string} today")
    public void uniqueSkiersPassedThroughToday(int count, String gateId) {
        for (long i = 1; i <= count; i++) {
            passageRepository.save(new Passage(i, LocalDateTime.now(),gateId));
        }
    }

    @When("I ask for the daily report of {string}")
    public void iAskForTheDailyReportOf(String gateId) {
        report = analyticsProducer.aggregateTodayStats(gateId);
    }

    @Then("the report should show {int} total passages")
    public void theReportShouldShowTotalPassages(int expected) {
        assertEquals(expected, report.totalPassages());
    }

    @Then("the report should show {int} unique skiers")
    public void theReportShouldShowUniqueSkiers(int expected) {
        assertEquals(expected, report.uniqueSkiers());
    }

    @Then("the opening time should be {string}")
    public void theOpeningTimeShouldBe(String expectedTime) {
        assertEquals(expectedTime, report.openingTime());
    }
}
