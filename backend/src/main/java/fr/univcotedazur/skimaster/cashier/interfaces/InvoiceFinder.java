package fr.univcotedazur.skimaster.cashier.interfaces;

import fr.univcotedazur.skimaster.cashier.entites.Invoice;
import java.util.List;

public interface InvoiceFinder {
    List<Invoice> findInvoicesByCustomerId(Long customerId);
}
