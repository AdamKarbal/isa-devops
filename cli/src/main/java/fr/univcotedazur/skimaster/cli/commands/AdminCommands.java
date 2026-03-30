package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import org.springframework.core.ParameterizedTypeReference;
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

    @ShellMethod(value = "Fermer un portique (close-gate + code)", key = "close-gate")
    public void closeGate(String code){
        this.restClient.post()
                .uri(BASE_URI + "/" + code + "/close")
                .retrieve()
                .toBodilessEntity();
        System.out.println("Le portique a été fermé.");
    }

    @ShellMethod(value = "Ouvrir un portique (open-gate + code)", key = "open-gate")
    public void openGate(String code){
        this.restClient.post()
                .uri(BASE_URI + "/" + code + "/open")
                .retrieve()
                .toBodilessEntity();

        System.out.println("Le portique a été ouvert.");
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

    //TODO: A compléter
    @ShellMethod(key = "Consulter les statistiques de fréquentation d'un portique en temps réel. (Ex: monitor-gate code)", value = "monitor-gate")
    public void monitor(String code){
        this.restClient.get()
                .uri(BASE_URI + "/monitor/" + code)
                .retrieve()
                .body();


    }
*/
    @ShellMethod(value = "Consulter les incidents", key = "healthcheck")
    public void checkGates(){
        List<String> iList = this.restClient.get()
                .uri(BASE_URI + "/alerts")
                .retrieve()
                .body(new ParameterizedTypeReference<List<String>>() {});

        if(iList.isEmpty()) System.out.println("Aucun incident signalé pour le moment.");
        else{
            StringBuilder stringBuilder = new StringBuilder();
            assert iList != null;

            for(String s : iList){
                stringBuilder.append(s);
                stringBuilder.append("\n");
            }

            System.out.println(stringBuilder);
        }
    }

}
