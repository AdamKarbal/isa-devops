package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import fr.univcotedazur.skimaster.cli.model.CliGate;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;

@ShellComponent
public class GateCommands {
    public static final String BASE_URI = "/gate";

    private final RestClient restClient;

    private final CliContext cliContext;

    public GateCommands(RestClient restClient, CliContext cliContext){
        this.restClient = restClient;
        this.cliContext = cliContext;
    }

    @ShellMethod("Open a gate (open-gate)")
    public CliGate openGate(){
        return restClient.post()
                .uri(BASE_URI + "/open")
                .retrieve()
                .body(CliGate.class);
    }

    @ShellMethod("Close a gate (close-gate)")
    public CliGate closeGate(){
        return restClient.post()
                .uri(BASE_URI + "/close")
                .retrieve()
                .body(CliGate.class);
    }
}
