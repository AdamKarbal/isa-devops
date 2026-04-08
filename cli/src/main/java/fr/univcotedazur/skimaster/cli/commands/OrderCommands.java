package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import fr.univcotedazur.skimaster.cli.model.CliInvoice;
import fr.univcotedazur.skimaster.cli.model.CliOrder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@ShellComponent
public class OrderCommands {

    public static final String BASE_URI = "/orders";

    private final RestClient restClient;
    private final CliContext cliContext;

    public OrderCommands(RestClient restClient, CliContext cliContext) {
        this.restClient = restClient;
        this.cliContext = cliContext;
    }

    @ShellMethod(value = "List all orders", key = "all-orders") // <- Ajout de la key
    public String orders() {
        try {
            List<CliOrder> orders = restClient.get()
                    .uri(BASE_URI)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<CliOrder>>() {});

            return orders.isEmpty() ? "No orders found." : orders.toString();
        } catch (Exception e) {
            return "Error retrieving orders: " + e.getMessage();
        }
    }

    @ShellMethod("Pay an existing order (pay-order ORDER_ID)")
    public String payOrder(Long orderId) {
        try {
            CliInvoice invoice = restClient.post()
                    .uri(BASE_URI + "/{id}/pay", orderId)
                    .retrieve()
                    .body(CliInvoice.class);

            return "SUCCESS: Order " + orderId + " has been paid! \nGenerated " + invoice.toString();
        } catch (HttpClientErrorException e) {
            return "PAYMENT ERROR: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "ERROR processing payment: " + e.getMessage();
        }
    }
}
