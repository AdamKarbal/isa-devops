package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@ShellComponent
public class GateCommands {

    public static final String BASE_URI = "/gates";

    private final RestClient restClient;
    private final CliContext cliContext;

    public GateCommands(RestClient restClient, CliContext cliContext) {
        this.restClient = restClient;
        this.cliContext = cliContext;
    }

    @ShellMethod("Simulate a badge beep at a gate")
    public String beep(Long cardId, Long gateId) {
        try {
            return "Beep processed for card " + cardId;
        } catch (Exception e) {
            return "Access Denied: " + e.getMessage();
        }
    }

    @ShellMethod("Synchronize the whitelist from database to cache")
    public String sync() {
        return "Whitelist synchronized!";
    }
}
