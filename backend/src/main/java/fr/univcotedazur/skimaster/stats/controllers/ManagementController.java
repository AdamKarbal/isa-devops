package fr.univcotedazur.skimaster.stats.controllers;

import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.stats.components.StatisticsProducer;
import fr.univcotedazur.skimaster.stats.dtos.DailyReportDTO;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProducer;
import fr.univcotedazur.skimaster.stats.interfaces.AnalyticsProvider;
import fr.univcotedazur.skimaster.stats.interfaces.DailyReport;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = ManagementController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class ManagementController {
    public static final String BASE_URI = "/stats";
    private final StatisticsProducer analyticsProducer;
    private final AnalyticsProvider analyticsProvider;

    public ManagementController(StatisticsProducer analyticsProducer, AnalyticsProvider analyticsProvider) {
        this.analyticsProducer=analyticsProducer;
        this.analyticsProvider=analyticsProvider;
    }

    @GetMapping("/today/{gateId}")
    public List<Passage> getTodayPassages(@PathVariable String gateId) {
        return analyticsProvider.getDailyPassagesByGate(gateId);
    }

    @GetMapping("/gates/{gateId}/passages/{day}")
    public List<Passage> getPassagesByDay(@PathVariable String gateId,@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return analyticsProvider.getGatePassages(day,gateId);
    }

    @GetMapping("/gates/{gateId}/opening-time")
    public String getOpeningTimeByGate(@PathVariable String gateId) {
        return analyticsProducer.aggregateOpeningTimeByGate(gateId);
    }

    @GetMapping("/passages/unique/today")
    public int getUniquePassages() {
        return analyticsProvider.getTotalUniqueNumberOfPassages();
    }

    @GetMapping("/gates/{gateId}/reports/daily")
    public DailyReportDTO getDailyReport(@PathVariable String gateId) {
        return analyticsProducer.aggregateTodayStats(gateId);
    }

    @GetMapping("/reports/daily/{day}/{gateId}")
    public DailyReportDTO getDailyReportByDay(@PathVariable String gateId,@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return analyticsProducer.aggregateStatsByDay(day,gateId);
    }

}
