package fr.univcotedazur.skimaster.cli.model;

import java.util.List;

public class CliOrderLine {
    private int quantity;
    private int unitPrice;
    private int lineTotal;

    public CliOrderLine(int quantity, int unitPrice, int lineTotal) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }
    public int getUnitPrice() {
        return unitPrice;
    }
    public int getLineTotal() {
        return lineTotal;
    }
    public int getQuantity() {
        return quantity;
    }
}
