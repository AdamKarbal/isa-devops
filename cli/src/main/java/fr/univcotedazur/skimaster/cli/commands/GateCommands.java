package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;

@ShellComponent
public class GateCommands {
    public static final String BASE_URI = "/gate";

    private final RestClient restClient;


    public GateCommands(RestClient restClient, CliContext cliContext){
        this.restClient = restClient;
    }

    @ShellMethod(value = "Open a gate", key="open-gate")
    public String openGate(){
        restClient.post()
                .uri(BASE_URI + "/open")
                .retrieve()
                .toBodilessEntity();

        return "Le portique a été ouvert avec succès.";
    }

    @ShellMethod(value = "Close a gate", key ="close-gate")
    public String closeGate(){
        restClient.post()
                .uri(BASE_URI + "/close")
                .retrieve()
                .toBodilessEntity();

        return "Le portique a été fermé avec succès.";
    }
}
