package fr.univcotedazur.skimaster.cashier.components;

import fr.univcotedazur.skimaster.cashier.entites.Invoice;
import fr.univcotedazur.skimaster.cashier.exceptions.PaymentException;
import fr.univcotedazur.skimaster.cashier.interfaces.InvoiceCreator;
import fr.univcotedazur.skimaster.cashier.interfaces.Payment;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.enums.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InvoiceHandler implements InvoiceCreator {

    private final Payment paymentService;
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceHandler.class);



    public InvoiceHandler(Payment paymentService){
        this.paymentService = paymentService;
    }

    public void processOrderPayment(Order order) {
        try {
            String receiptId = paymentService.payOrderFromCart(order.getCustomer(), order.getPrice());
            LOG.info("Paiement accepté. Reçu banque : {}", receiptId);
            order.setPaymentReceiptId(receiptId);
            order.setStatus(OrderStatus.PAID);
            LOG.info("Facture générée : ID {}", receiptId);
        } catch (PaymentException e) { // ICII IHANDLER CAS PAYMENT MARCHE PAS
            LOG.error("Échec du paiement pour le client {} : {}", order.getCustomer().getName(), e.getMessage());
            order.setStatus(OrderStatus.PAYMENT_FAILED);
        }
    }


    @Override
    public Invoice createInvoice(Order order, String receiptId) {
        Invoice invoice = new Invoice(++idCounter, order);
        invoiceDatabase.put(invoice.getInvoiceID(), invoice);
        return invoice;
    }

    @Override
    public Invoice getInvoice(long invoiceID) {
        return invoiceDatabase.get(invoiceID);
    }

}
