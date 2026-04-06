package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import fr.univcotedazur.skimaster.cli.model.CliForfaitOffer;
import fr.univcotedazur.skimaster.cli.model.CliProduct;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;

import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ShellComponent
public class CatalogCommands {

    private static final String BASE_URI = "/catalog";

    private final RestClient restClient;
    private final CliContext cliContext;

    public CatalogCommands(RestClient restClient, CliContext cliContext) {
        this.restClient = restClient;
        this.cliContext = cliContext;
    }

    @ShellMethod("Create a forfait offer (add-offer AGE_CATEGORY PASS_TYPE PRICE)")
    public CliForfaitOffer addOffer(String ageCategory, String passType, double price) {
        try {
            CliForfaitOffer created = restClient.post()
                    .uri(BASE_URI + "/offers")
                    .body(new CliForfaitOffer(ageCategory, passType, price))
                    .retrieve()
                    .body(CliForfaitOffer.class);
            CliForfaitOffer offer = Objects.requireNonNull(created);
            cliContext.getOffers().put(offer.getId(), offer);
            return offer;
        } catch (HttpClientErrorException.Conflict e) {
            System.out.println("Offre deja existante, ignoree.");
            return null;
        }
    }

    @ShellMethod("Create a product from a comma-separated list of offer ids (add-product OFFER_IDS_CSV REDUCTION)")
    public CliProduct addProduct(String offerIdsCsv, int reduction) {
        List<Long> offerIds = Arrays.stream(offerIdsCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(Long::parseLong)
                .toList();

        CliProduct created = restClient.post()
                .uri(BASE_URI + "/products")
                .body(new CliProduct(offerIds, reduction))
                .retrieve()
                .body(CliProduct.class);

        CliProduct product = Objects.requireNonNull(created);
        cliContext.getProducts().put(product.getId(), product);
        return product;
    }

    @ShellMethod("List locally known catalog offers")
    public String offers() {
        return cliContext.getOffers().values().stream()
                .map(CliForfaitOffer::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    @ShellMethod("List locally known catalog products")
    public String products() {
        return cliContext.getProducts().values().stream()
                .map(CliProduct::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
