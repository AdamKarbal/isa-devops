package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import fr.univcotedazur.skimaster.cli.model.CliCustomer;
import fr.univcotedazur.skimaster.cli.model.CliForfait;
import fr.univcotedazur.skimaster.cli.model.CliOrderCreated;
import fr.univcotedazur.skimaster.cli.model.CliProduct;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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

    @ShellMethod(key = "register", value = "Register a customer in the CoD backend (register CUSTOMER_NAME CREDIT_CARD_NUMBER)")
    public CliCustomer register(String name, String creditCard) {
        CliCustomer res = restClient.post()
                .uri(BASE_URI)
                .body(new CliCustomer(name, creditCard))
                .retrieve()
                .body(CliCustomer.class);
        cliContext.getCustomers().put(Objects.requireNonNull(res).getName(), res);
        return res;
    }

    @ShellMethod(key = "customers", value = "List all customers (customers)")
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

            if (customer == null) return "client introuvable";

            return "Client #" + customer.getId() + System.lineSeparator()
                    + "Nom: " + customer.getName() + System.lineSeparator()
                    + "CB: " + customer.getCreditCard();

        } catch (HttpClientErrorException.NotFound e) {
            return "client introuvable";
        }
    }

    @ShellMethod(key = "passes", value = "List passes for a customer (passes CUSTOMER_ID)")
    public String passes(@ShellOption long customerId) {
        try {
            CliForfait[] passes = restClient.get()
                    .uri(BASE_URI + "/{id}/forfaits", customerId)
                    .retrieve()
                    .body(CliForfait[].class);

            if (passes == null || passes.length == 0) {
                return "Aucun forfait pour ce client";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Forfaits du client #").append(customerId).append(System.lineSeparator());

            for (CliForfait p : passes) {
                sb.append("- F").append(p.getId())
                        .append(" type=").append(p.getPassType())
                        .append(" age=").append(p.getAgeCategory())
                        .append(" date=").append(p.getValidDate())
                        .append(System.lineSeparator());
            }
            return sb.toString().trim();

        } catch (HttpClientErrorException.NotFound e) {
            return "client introuvable";
        }
    }

    @ShellMethod(key = "orders", value = "List orders for a customer (orders CUSTOMER_ID)")
    public String orders(@ShellOption long customerId) {
        try {
            CliOrderCreated[] orders = restClient.get()
                    .uri(BASE_URI + "/{id}/orders", customerId)
                    .retrieve()
                    .body(CliOrderCreated[].class);

            if (orders == null || orders.length == 0) {
                return "Aucune commande pour ce client";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Historique des commandes du client #").append(customerId).append(System.lineSeparator());

            for (CliOrderCreated o : orders) {
                sb.append("- commande #").append(o.getId())
                        .append(" montant=").append(o.getPrice())
                        .append(System.lineSeparator());

                Set<CliProduct> items = o.getItems();
                if (items != null && !items.isEmpty()) {
                    for (CliProduct product : items) {
                        sb.append("  * produit #")
                                .append(product.getId())
                                .append(" reduction=")
                                .append(product.getReduction())
                                .append(System.lineSeparator());
                    }
                }
            }
            return sb.toString().trim();

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
                    .uri(BASE_URI + "/{cid}/forfaits/{pid}", customerId, passId)
                    .retrieve()
                    .body(CliForfait.class);

            if (p == null) return "forfait introuvable";

            return "Forfait F" + p.getId() + System.lineSeparator()
                    + "Client: " + p.getCustomerId() + System.lineSeparator()
                    + "Type: " + p.getPassType() + System.lineSeparator()
                    + "Age: " + p.getAgeCategory() + System.lineSeparator()
                    + "Date: " + p.getValidDate();

        } catch (HttpClientErrorException.NotFound e) {
            return "forfait introuvable";
        }
    }

    @ShellMethod(key = "order show", value = "Display a specific order of a customer (order show CUSTOMER_ID ORDER_ID)")
    public String orderShow(@ShellOption long customerId, @ShellOption long orderId) {
        try {
            CliOrderCreated o = restClient.get()
                    .uri(BASE_URI + "/{cid}/orders/{oid}", customerId, orderId)
                    .retrieve()
                    .body(CliOrderCreated.class);

            if (o == null) return "commande introuvable";

            StringBuilder sb = new StringBuilder();
            sb.append("Commande #").append(o.getId()).append(System.lineSeparator());
            sb.append("Montant: ").append(o.getPrice()).append(System.lineSeparator());

            Set<CliProduct> items = o.getItems();
            if (items != null && !items.isEmpty()) {
                sb.append("Produits:").append(System.lineSeparator());
                for (CliProduct product : items) {
                    sb.append("  * produit #")
                            .append(product.getId())
                            .append(" reduction=")
                            .append(product.getReduction())
                            .append(System.lineSeparator());
                }
            }

            return sb.toString().trim();

        } catch (HttpClientErrorException.NotFound e) {
            return "commande introuvable";
        }
    }

    @ShellMethod(key = "set-supercarte", value = "Enable or disable SuperCarte for a customer (set-supercarte CUSTOMER_NAME true/false)")
    public String setSuperCarte(String name, boolean status) {
        try {
            // On cherche le client dans le contexte local par son nom
            CliCustomer customer = cliContext.getCustomers().get(name);
            if (customer == null) {
                return "ERREUR : Client '" + name + "' inconnu. Avez-vous fait un 'update-customers' ?";
            }
            restClient.put()
                    .uri(BASE_URI + "/{id}/supercarte?status={status}", customer.getId(), status)
                    .retrieve()
                    .toBodilessEntity();

            return "SUCCÈS : SuperCarte " + (status ? "activée" : "désactivée") + " pour " + name;

        } catch (HttpClientErrorException.NotFound e) {
            return "ERREUR : Client non trouvé sur le serveur";
        } catch (Exception e) {
            return "ERREUR : " + e.getMessage();
        }
    }
}
