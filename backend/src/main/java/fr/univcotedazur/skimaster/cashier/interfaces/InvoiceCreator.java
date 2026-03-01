package fr.univcotedazur.skimaster.cashier.interfaces;

import fr.univcotedazur.skimaster.cashier.entites.Invoice;
import fr.univcotedazur.skimaster.order.entities.Order;

public interface InvoiceCreator {
    Invoice processOrderPayment(Order order);
}
