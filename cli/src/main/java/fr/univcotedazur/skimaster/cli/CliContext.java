package fr.univcotedazur.skimaster.cli;

import fr.univcotedazur.skimaster.cli.model.CliCustomer;
import fr.univcotedazur.skimaster.cli.model.CliForfaitOffer;
import fr.univcotedazur.skimaster.cli.model.CliProduct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class    CliContext {

    private Map<String, CliCustomer> customers;
    private Map<Long, CliForfaitOffer> offers;
    private Map<Long, CliProduct> products;

    public Map<String, CliCustomer> getCustomers() {
        return customers;
    }

    public Map<Long, CliForfaitOffer> getOffers() {
        return offers;
    }

    public Map<Long, CliProduct> getProducts() {
        return products;
    }

    public CliContext() {
        customers = new HashMap<>();
        offers = new HashMap<>();
        products = new HashMap<>();
    }

    @Override
    public String toString() {
        return customers.keySet().stream()
                .map(key -> key + "=" + customers.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }

}
