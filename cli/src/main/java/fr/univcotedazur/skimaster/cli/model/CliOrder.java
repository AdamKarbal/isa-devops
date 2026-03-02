package fr.univcotedazur.skimaster.cli.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.ArrayList;
import java.util.List;

/**
 * CLI-side representation aligned with an OrderCreatedDTO-like payload
 * containing an order identifier, ordered item identifiers and total price.
 */
public class CliOrder {

    @JsonAlias({"orderId", "id"})
    private Long orderId;    private List<CliProduct> items = new ArrayList<>();
    private double price;
    private String status;

    public CliOrder() {
    }

    public CliOrder(Long orderId, List<CliProduct> items, double price, String status) {
        this.orderId = orderId;
        this.items = new ArrayList<>(items);
        this.price = price;
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<CliProduct> getItems() {
        return items;
    }

    public void setItems(List<CliProduct> items) {
        this.items = items;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    @Override
    public String toString() {
        List<Long> itemIds = items.stream().map(CliProduct::getId).toList();
        return "Order{" +
                "orderId=" + orderId +
                ", status=" + status +
                ", itemIds=" + itemIds +
                ", price=" + price +
                '}';
    }
}
