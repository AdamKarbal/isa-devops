package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.model.CliDailyReport;
import fr.univcotedazur.skimaster.cli.model.CliStatsPassage;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

@ShellComponent
public class ManagementCommands {

    private static final String BASE_URI = "/stats";

    private final RestClient restClient;

    public ManagementCommands(RestClient restClient) {
        this.restClient = restClient;
    }

    @ShellMethod(value = "Get today's passages for a gate (stats-today-passages GATE_ID)", key = "stats-today-passages")
    public String getTodayPassages(String gateId) {
        try {
            CliStatsPassage[] passages = restClient.get()
                    .uri(BASE_URI + "/today/{gateId}", gateId)
                    .retrieve()
                    .body(CliStatsPassage[].class);
            return formatPassages(passages);
        } catch (Exception e) {
            return "ERROR retrieving today's passages: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Get passages for a gate on a given day (stats-passages-by-day GATE_ID YYYY-MM-DD)", key = "stats-passages-by-day")
    public String getPassagesByDay(String gateId, String day) {
        try {
            LocalDate parsedDay = LocalDate.parse(day);
            CliStatsPassage[] passages = restClient.get()
                    .uri(BASE_URI + "/gates/{gateId}/passages/{day}", gateId, parsedDay)
                    .retrieve()
                    .body(CliStatsPassage[].class);
            return formatPassages(passages);
        } catch (Exception e) {
            return "ERROR retrieving passages by day: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Get opening time by gate (stats-opening-time GATE_ID)", key = "stats-opening-time")
    public String getOpeningTimeByGate(String gateId) {
        try {
            String openingTime = restClient.get()
                    .uri(BASE_URI + "/gates/{gateId}/opening-time", gateId)
                    .retrieve()
                    .body(String.class);
            return "Opening time for gate " + gateId + ": " + openingTime;
        } catch (Exception e) {
            return "ERROR retrieving opening time: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Get total unique passages today (stats-unique-passages)", key = "stats-unique-passages")
    public String getUniquePassages() {
        try {
            Integer uniquePassages = restClient.get()
                    .uri(BASE_URI + "/passages/unique/today")
                    .retrieve()
                    .body(Integer.class);
            return "Unique passages today: " + uniquePassages;
        } catch (Exception e) {
            return "ERROR retrieving unique passages: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Get today's daily report for a gate (stats-daily-report GATE_ID)", key = "stats-daily-report")
    public String getDailyReport(String gateId) {
        try {
            CliDailyReport report = restClient.get()
                    .uri(BASE_URI + "/gates/{gateId}/reports/daily", gateId)
                    .retrieve()
                    .body(CliDailyReport.class);
            return Objects.requireNonNull(report).toString();
        } catch (Exception e) {
            return "ERROR retrieving today's daily report: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Get daily report for a gate on a given day (stats-daily-report-by-day GATE_ID YYYY-MM-DD)", key = "stats-daily-report-by-day")
    public String getDailyReportByDay(String gateId, String day) {
        try {
            LocalDate parsedDay = LocalDate.parse(day);
            CliDailyReport report = restClient.get()
                    .uri(BASE_URI + "/reports/daily/{day}/{gateId}", parsedDay, gateId)
                    .retrieve()
                    .body(CliDailyReport.class);
            return Objects.requireNonNull(report).toString();
        } catch (Exception e) {
            return "ERROR retrieving daily report by day: " + e.getMessage();
        }
    }

    private String formatPassages(CliStatsPassage[] passages) {
        if (passages == null || passages.length == 0) {
            return "No passages found.";
        }
        return Arrays.toString(passages);
    }
}
