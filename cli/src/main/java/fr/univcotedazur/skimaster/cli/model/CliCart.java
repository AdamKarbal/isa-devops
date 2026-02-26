package fr.univcotedazur.skimaster.cli.model;

import java.util.List;

public class CliCart {

    private List<CartElement> items;

    public CliCart() {}

    public List<CartElement> getItems() {
        return items;
    }

    public void setItems(List<CartElement> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CliCart{" +
                "items=" + items +
                '}';
    }
}
