package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import fr.univcotedazur.skimaster.cli.model.CliCustomer;
import fr.univcotedazur.skimaster.cli.model.CliForfait;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@ShellComponent
public class CustomerCommands {

    public static final String BASE_URI = "/customers";

    private final RestClient restClient;

    private final CliContext cliContext;

    public CustomerCommands(RestClient restClient, CliContext cliContext) {
        this.restClient = restClient;
        this.cliContext = cliContext;
    }

    @ShellMethod("Register a customer in the CoD backend (register CUSTOMER_NAME CREDIT_CARD_NUMBER)")
    public CliCustomer register(String name, String creditCard) {
        CliCustomer res = restClient.post()
                .uri(BASE_URI)
                .body(new CliCustomer(name, creditCard))
                .retrieve()
                .body(CliCustomer.class);
        cliContext.getCustomers().put(Objects.requireNonNull(res).getName(), res);
        return res;
    }

    @ShellMethod("List all known customers")
    public String customers() {
        return cliContext.getCustomers().toString();
    }

    @ShellMethod("Update all known customers from server")
    public String updateCustomers() {
        Map<String, CliCustomer> customerMap = cliContext.getCustomers();
        customerMap.clear();
        CliCustomer[] customers = restClient
                .get()
                .uri(BASE_URI)
                .retrieve()
                .body(CliCustomer[].class);
        customerMap.putAll(Arrays.stream(customers)
                        .collect(Collectors.toMap(CliCustomer::getName, Function.identity()))
        );
        return customerMap.toString();
    }

    @ShellMethod(key = "customer show", value = "Display customer profile (customer show CUSTOMER_ID)")
    public String show(@ShellOption long id) {
        try {
            CliCustomer customer = restClient.get()
                    .uri(BASE_URI + "/{id}", id)
                    .retrieve()
                    .body(CliCustomer.class);

            if (customer == null) {
                return "client introuvable";
            }

            return "Customer #" + customer.getId() + System.lineSeparator()
                    + "Name: " + customer.getName();
        } catch (HttpClientErrorException.NotFound e) {
            return "client introuvable";
        }
    }

    @ShellMethod(key = "passes", value = "List passes for a customer (passes CUSTOMER_ID)")
    public String passes(@ShellOption long customerId) {
        try {
            CliForfait[] passes = restClient.get()
                    .uri("/customers/{id}/forfaits", customerId)
                    .retrieve()
                    .body(CliForfait[].class);

            if (passes == null || passes.length == 0) {
                return "Aucun forfait";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Passes for customer #").append(customerId).append(System.lineSeparator());
            for (CliForfait p : passes) {
                sb.append("- ")
                        .append("P").append(p.getId()).append(" ")
                        .append(p.getPassType()).append(" ")
                        .append(p.getAgeCategory()).append(" ")
                        .append(p.getValidDate()).append(" ")
                        .append(p.getStatus())
                        .append(System.lineSeparator());
            }
            return sb.toString().trim();

        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            return "client introuvable";
        }
    }
}
