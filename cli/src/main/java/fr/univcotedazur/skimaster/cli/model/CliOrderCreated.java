package fr.univcotedazur.skimaster.cli.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Set;

public class CliOrderCreated {
    @JsonAlias({"orderId", "id"})
    private Long orderId;
    private Set<CliProduct> items;
    private double price;
    private String status;

    public CliOrderCreated() {}

    public Long getId() { return orderId; }
    public Set<CliProduct> getItems() { return items; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }

    public void setId(Long orderId) { this.orderId = orderId; }
    public void setItems(Set<CliProduct> items) { this.items = items; }
    public void setPrice(double price) { this.price = price; }
    public void setStatus(String status) { this.status = status; }
}
