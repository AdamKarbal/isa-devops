package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import fr.univcotedazur.skimaster.cli.model.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;

import java.util.List;

@ShellComponent
public class AdminCommands {
    private static final String BASE_URI = "/admin";
    private static final String GATE_BASE_URI = "/gates";
    private static final String STATS_BASE_URI = "/stats";

    private final RestClient restClient;

    public AdminCommands(RestClient restClient, CliContext cliContext) {
        this.restClient = restClient;
    }

    @ShellMethod(value = "Fermer un portique (close-gate + code)", key = "close-gate")
    public void closeGate(String code){
        this.restClient.post()
                .uri(BASE_URI + "/" + code + "/close")
                .retrieve()
                .toBodilessEntity();
        System.out.println("Le portique a été fermé.");
    }

    @ShellMethod(key = "open-gate", value = "Ouvrir un portique (open-gate CODE)")
    public String openGate(String code) {
        try {
            this.restClient.post()
                    .uri(BASE_URI + "/" + code + "/open")
                    .retrieve()
                    .toBodilessEntity();
            return "Le portique a ete ouvert.";
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            return "Impossible d'ouvrir le portique " + code + " : " + e.getResponseBodyAsString();
        }
    }

    @ShellMethod(value = "Ouvrir tous les portiques", key = "open-all-gates")
    public void openAllGate(){
        this.restClient.post()
                .uri(BASE_URI + "/openAll")
                .retrieve()
                .toBodilessEntity();
        System.out.println("Tous les portiques ont été ouverts.");
    }

    @ShellMethod(value = "Fermer tous les portiques", key = "close-all-gates")
    public void closeAllGate(){
        this.restClient.post()
                .uri(BASE_URI + "/closeAll")
                .retrieve()
                .toBodilessEntity();

        System.out.println("Tous les portiques ont été fermés.");
    }

    @ShellMethod(value = "Résoudre un incident sur un portique (ex : resolve gate-1)", key = "resolve")
    public void resolve(String code){
        this.restClient.post()
                .uri(BASE_URI + "/resolve/" + code)
                .retrieve()
                .toBodilessEntity();

        System.out.println("Problème résolu et portique réouvert.");
    }

    /*
    //TODO: A compléter
    @ShellMethod(key = "Consulter les statistiques globales de fréquentation en temps réel.", value = "monitor-all")
    public void monitorAll(){
        this.restClient.get()
                .uri(BASE_URI + "/monitorAll")
                .retrieve()
                .body();


    }
     */

    @ShellMethod(key = "list-displays", value = "Lister les displays connus (par code de portique)")
    public String listDisplays() {
        List<String> displays = this.restClient.get()
                .uri(BASE_URI + "/display/list")
                .retrieve()
                .body(new ParameterizedTypeReference<List<String>>() {});

        if (displays == null || displays.isEmpty()) {
            return "Aucun display connu pour le moment (aucun message recu).";
        }

        StringBuilder builder = new StringBuilder("--- DISPLAYS ---\n");
        for (String displayCode : displays) {
            builder.append("ID: ").append(displayCode).append('\n');
        }
        return builder.toString();
    }

    /*
    //TODO: A compléter
    @ShellMethod(key = "Consulter les statistiques de fréquentation d'un portique en temps réel. (Ex: monitor-gate code)", value = "monitor-gate")
    public void monitor(String code){
        this.restClient.get()
                .uri(BASE_URI + "/monitor/" + code)
                .retrieve()
                .body();
*/



    @ShellMethod(key = "Consulter les incidents", value = "healthcheck")
    public void checkGates(){
        List<String> iList = this.restClient.get()
                .uri(BASE_URI + "/alerts")
                .retrieve()
                .body(new ParameterizedTypeReference<List<String>>() {});

        StringBuilder stringBuilder = new StringBuilder();
        assert iList != null;
        for(String s : iList){
            stringBuilder.append(s);
            stringBuilder.append("\n");
        }

        System.out.println(stringBuilder);
    }

    @ShellMethod(key = "display-message-target", value = "Envoyer un message a un display precis (display-message-target DISPLAY_CODE \"MESSAGE\")")
    public String sendDisplayMessageTarget(String displayCode, String message) {
        this.restClient.post()
                .uri(BASE_URI + "/display/message")
                .body(new AdminDisplayMessageDTO(message, displayCode))
                .retrieve()
                .toBodilessEntity();
        return "Message envoye au display " + displayCode + ".";
    }


    @ShellMethod(key = "display-clear", value = "Effacer les messages des displays")
    public String clearDisplay() {
        this.restClient.delete()
                .uri(BASE_URI + "/display/message")
                .retrieve()
                .toBodilessEntity();
        return "Display efface.";
    }

    @ShellMethod(key = "display-show-all", value = "Afficher tous les messages des displays")
    public String displayShowAll() {
        List<DisplayAlertDTO> alerts = this.restClient.get()
                .uri(BASE_URI + "/display/alerts")
                .retrieve()
                .body(new ParameterizedTypeReference<List<DisplayAlertDTO>>() {});

        return formatDisplayAlerts(alerts, "TOUS");
    }

    @ShellMethod(key = "display-show", value = "Afficher les messages d'un display precis (display-show DISPLAY_CODE)")
    public String displayShow(String displayCode) {
        List<DisplayAlertDTO> alerts = this.restClient.get()
                .uri(BASE_URI + "/display/alerts/" + displayCode)
                .retrieve()
                .body(new ParameterizedTypeReference<List<DisplayAlertDTO>>() {});

        return formatDisplayAlerts(alerts, displayCode);
    }

    @ShellMethod(key = "set-alert-threshold", value = "Mettre a jour le seuil d'alerte d'un portique (set-alert-threshold GATE_CODE THRESHOLD)")
    public String setAlertThreshold(String gateCode, int alertThreshold) {
        GateMonitoringStatusDTO status = this.restClient.put()
                .uri(GATE_BASE_URI + "/" + gateCode + "/alert-threshold")
                .body(new GateAlertThresholdRequestDTO(alertThreshold))
                .retrieve()
                .body(GateMonitoringStatusDTO.class);

        if (status == null) {
            return "Seuil mis a jour, mais aucun statut n'a ete retourne.";
        }

        return "Seuil mis a jour pour " + status.gateId() + ": threshold=" + status.alertThreshold() + ", status=" + status.status();
    }

    @ShellMethod(key = "list-alert-thresholds", value = "Consulter les seuils des portiques")
    public String listAlertThresholds() {
        List<GateMonitoringStatusDTO> statuses = this.restClient.get()
                .uri(GATE_BASE_URI + "/status")
                .retrieve()
                .body(new ParameterizedTypeReference<List<GateMonitoringStatusDTO>>() {});

        if (statuses == null || statuses.isEmpty()) {
            return "Aucun portique configure.";
        }

        StringBuilder builder = new StringBuilder("--- GATES ---\n");
        for (GateMonitoringStatusDTO status : statuses) {
            builder.append(status.gateId())
                    .append(" status=")
                    .append(status.status())
                    .append(" alert-threshold=")
                    .append(status.alertThreshold())
                    .append('\n');
        }
        return builder.toString();
    }

    @ShellMethod(key = "monitor-all", value = "Consulter les statistiques globales de frequentation en temps reel")
    public String monitorAll() {
        DailyReportDTO report = this.restClient.get()
                .uri(STATS_BASE_URI + "/reports/daily")
                .retrieve()
                .body(DailyReportDTO.class);

        if (report == null) {
            return "Aucune statistique disponible.";
        }
        return "Stats du " + report.date()
                + ": totalPassages=" + report.totalPassages()
                + ", uniqueSkiers=" + report.uniqueSkiers()
                + ", activeForfaits=" + report.activeForfaits();
    }

    @ShellMethod(key = "monitor-gate", value = "Consulter les statistiques de frequentation d'un portique (monitor-gate CODE)")
    public String monitor(String code) {
        String openingTime = this.restClient.get()
                .uri(STATS_BASE_URI + "/opening-time/" + code)
                .retrieve()
                .body(String.class);

        return "Monitoring " + code + ": " + openingTime;
    }

    private static String formatDisplayAlerts(List<DisplayAlertDTO> alerts, String displayCode) {
        if (alerts == null || alerts.isEmpty()) {
            return "Aucun message pour le display " + displayCode + ".";
        }

        StringBuilder builder = new StringBuilder("--- DISPLAY ")
                .append(displayCode)
                .append(" ---\n");
        for (DisplayAlertDTO alert : alerts) {
            builder.append('[')
                    .append(alert.receivedAt())
                    .append("] ")
                    .append(alert.gateCode())
                    .append(" -> ")
                    .append(alert.message())
                    .append('\n');
        }
        return builder.toString();
    }

}
