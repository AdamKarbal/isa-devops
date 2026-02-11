package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import fr.univcotedazur.skimaster.cli.model.CliCustomer;
import fr.univcotedazur.skimaster.cli.model.CliForfait;
import fr.univcotedazur.skimaster.cli.model.CliOrder;
import fr.univcotedazur.skimaster.cli.model.CliOrderLine;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;
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
            sb.append("Passes pour le client #").append(customerId).append(System.lineSeparator());
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

    @ShellMethod(key = "orders", value = "List orders for a custolmer (orders CUSTOMER_ID)")
    public String orders(@ShellOption long customerId) {
        try {
            CliOrder[] orders = restClient.get()
                    .uri("/customers/{id}/orders", customerId)
                    .retrieve()
                    .body(CliOrder[].class);

            if (orders == null || orders.length == 0) {
                return "Aucune order pour ce customer";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Orders pour le client #").append(customerId).append("\n");

            for (CliOrder o : orders) {
                sb.append("- order #").append(o.getId())
                        .append(" state=").append(o.getState())
                        .append(" total=").append(o.getTotal())
                        .append("\n");

                for (CliOrderLine l : o.getLines()) {
                    sb.append("  * ")
                            .append(l.getQuantity()).append(" x ")
                            .append(" @ ").append(l.getUnitPrice())
                            .append(" = ").append(l.getLineTotal())
                            .append("\n");
                }
            }
            return sb.toString();

        } catch (HttpClientErrorException.NotFound e) {
            return "client introuvable";
        } catch (HttpClientErrorException e) {
            return "ERROR HTTP " + e.getStatusCode() + ": " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "ERROR: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }

    @ShellMethod(key = "pass show", value = "Display a specific pass of a customer (pass show CUSTOMER_ID PASS_ID)")
    public String passShow(@ShellOption long customerId, @ShellOption long passId) {
        try {
            CliForfait p = restClient.get()
                    .uri("/customers/{cid}/forfaits/{pid}", customerId, passId)
                    .retrieve()
                    .body(CliForfait.class);

            if (p == null) return "forfait introuvable";

            return "Pass P" + p.getId() + System.lineSeparator()
                    + "Type: " + p.getPassType() + System.lineSeparator()
                    + "Age: " + p.getAgeCategory() + System.lineSeparator()
                    + "ValidDate: " + p.getValidDate() + System.lineSeparator()
                    + "Status: " + p.getStatus();

        } catch (HttpClientErrorException.NotFound e) {
            return "forfait introuvable";
        }
    }

    @ShellMethod(key = "order show", value = "Display a specific order of a customer (order show CUSTOMER_ID ORDER_ID)")
    public String orderShow(@ShellOption long customerId, @ShellOption long orderId) {
        try {
            CliOrder o = restClient.get()
                    .uri("/customers/{cid}/orders/{oid}", customerId, orderId)
                    .retrieve()
                    .body(CliOrder.class);

            if (o == null) return "order introuvable";

            StringBuilder sb = new StringBuilder();
            sb.append("Order #").append(o.getId()).append(System.lineSeparator());
            sb.append("State: ").append(o.getState()).append(System.lineSeparator());
            sb.append("Total: ").append(o.getTotal()).append(System.lineSeparator());

            if (o.getLines() != null && !o.getLines().isEmpty()) {
                sb.append("Lines:").append(System.lineSeparator());
                for (CliOrderLine l : o.getLines()) {
                    sb.append("  * ")
                            .append(l.getQuantity()).append(" x ")
                            .append("@ ").append(l.getUnitPrice())
                            .append(" = ").append(l.getLineTotal())
                            .append(System.lineSeparator());
                }
            }
            return sb.toString().trim();

        } catch (HttpClientErrorException.NotFound e) {
            return "order introuvable";
        }
    }
}
