package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import fr.univcotedazur.skimaster.incident.entities.Incident;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.component.view.control.ListView;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;

import java.util.List;

@ShellComponent
public class AdminCommands {
    private static final String BASE_URI = "/admin";

    private final RestClient restClient;

    private final CliContext cliContext;

    public AdminCommands(RestClient restClient, CliContext cliContext){
        this.restClient = restClient;
        this.cliContext = cliContext;
    }

    @ShellMethod(key = "Fermer un portique (close-gate + code)", value = "close-gate")
    public void closeGate(String code){
        this.restClient.post()
                .uri(BASE_URI + "/" + code + "/close")
                .retrieve()
                .toBodilessEntity();
        System.out.println("Le portique a été fermé.");
    }

    @ShellMethod(key = "Ouvrir un portique (open-gate + code)", value = "open-gate")
    public void openGate(String code){
        this.restClient.post()
                .uri(BASE_URI + "/" + code + "/open")
                .retrieve()
                .toBodilessEntity();

        System.out.println("Le portique a été ouvert.");
    }

    @ShellMethod(key = "Ouvrir tous les portiques", value = "open-all-gates")
    public void openAllGate(){
        this.restClient.post()
                .uri(BASE_URI + "/openAll")
                .retrieve()
                .toBodilessEntity();
        System.out.println("Tous les portiques ont été ouverts.");
    }

    @ShellMethod(key = "Fermer tous les portiques", value = "close-all-gates")
    public void closeAllGate(){
        this.restClient.post()
                .uri(BASE_URI + "/closeAll")
                .retrieve()
                .toBodilessEntity();

        System.out.println("Tous les portiques ont été fermés.");
    }


    @ShellMethod(key = "Résoudre un incident sur un portique (ex : resolve gate-1)", value = "resolve")
    public void resolve(String code){
        this.restClient.post()
                .uri(BASE_URI + "/resolve/" + code)
                .retrieve()
                .toBodilessEntity();

        System.out.println("Problème résolu et portique réouvert.");
    }

    @ShellMethod(key = "Consulter les incidents", value = "healthcheck")
    public void checkGates(){
        List<String> iList = this.restClient.get()
                .uri(BASE_URI + "/alerts")
                .retrieve()
                .body(new ParameterizedTypeReference<List<String>>() {});

        StringBuilder stringBuilder = new StringBuilder();
        for(String s : iList){
            stringBuilder.append(s);
            stringBuilder.append("\n");
        }

        System.out.println(stringBuilder);
    }

}
