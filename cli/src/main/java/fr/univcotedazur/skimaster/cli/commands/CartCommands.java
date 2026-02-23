package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import fr.univcotedazur.skimaster.cli.model.CartElement;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;
@ShellComponent
public class CartCommands {
    public static final String BASE_URI = "/cart";

    private final RestClient restClient;

    private final CliContext cliContext;
    public CartCommands(RestClient restClient, CliContext cliContext) {
        this.restClient = restClient;
        this.cliContext = cliContext;
    }
    @ShellMethod("Add cookie to cart of customer (add-to-cart CUSTOMER_NAME COOKIE_NAME QUANTITY")
    public CartElement addToCart(String name, Long productId, int quantity) {
        // Spring-shell is catching exception (could be the case if name is not from a valid customer)
        return restClient.post()
                .uri(getUriForCustomer(name)+"/add")
                .body(new CartElement(productId, quantity))
                .retrieve()
                .body(CartElement.class);
    }

    private String getUriForCustomer(String name) {
        return BASE_URI+"/"+cliContext.getCustomers().get(name).getId();
    }
}
