package fr.univcotedazur.skimaster.cli.model;

import java.util.Set;

public class CliOrderCreated {
    private Long orderId;
    private Set<CliProduct> items;
    private double price;

    public CliOrderCreated() {}

    public Long getId() { return orderId; }
    public Set<CliProduct> getItems() { return items; }
    public double getPrice() { return price; }

    public void setId(Long orderId) { this.orderId = orderId; }
    public void setItems(Set<CliProduct> items) { this.items = items; }
    public void setPrice(double price) { this.price = price; }
}

