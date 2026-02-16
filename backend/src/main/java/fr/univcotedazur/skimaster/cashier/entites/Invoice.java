package fr.univcotedazur.skimaster.cashier.entites;

import fr.univcotedazur.skimaster.order.entities.Order;

public class Invoice {
    private long InvoiceID;
    private Order order;

    public Invoice(long InvoiceID){
        this.InvoiceID=InvoiceID;
    }

    public long getInvoiceID(){
        return InvoiceID;
    }
}
