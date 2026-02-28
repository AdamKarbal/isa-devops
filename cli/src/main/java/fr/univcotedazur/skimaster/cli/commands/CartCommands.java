package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import fr.univcotedazur.skimaster.cli.model.CartElement;
import fr.univcotedazur.skimaster.cli.model.CliCart;
import fr.univcotedazur.skimaster.cli.model.CliOrder;
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

    @ShellMethod("Get the client's cart (getCart CUSTOMER_NAME)")
    public CliCart showCart(String name){
        return restClient.get().uri(getUriForCustomer(name)).retrieve().body(CliCart.class);
    }

    @ShellMethod("Validate the order (validate CUSTOMER_NAME)")
    public CliOrder validate(String name){
        return restClient.post()
                .uri(getUriForCustomer(name)+"/validate")
                .retrieve()
                .body(CliOrder.class);
    }

    private String getUriForCustomer(String name) {
        return BASE_URI+"/"+cliContext.getCustomers().get(name).getId();
    }
}
