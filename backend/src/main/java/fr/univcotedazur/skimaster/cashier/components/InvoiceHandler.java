package fr.univcotedazur.skimaster.cashier.components;

import fr.univcotedazur.skimaster.cashier.entites.Invoice;
import fr.univcotedazur.skimaster.cashier.exceptions.PaymentException;
import fr.univcotedazur.skimaster.cashier.interfaces.InvoiceCreator;
import fr.univcotedazur.skimaster.cashier.interfaces.Payment;
import fr.univcotedazur.skimaster.cashier.repositories.InvoiceRepository;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.enums.OrderStatus;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceHandler implements InvoiceCreator {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceHandler.class);
    private final Payment paymentService;
    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;

    public InvoiceHandler(Payment paymentService, OrderRepository orderRepository, InvoiceRepository invoiceRepository) {
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    @Transactional
    public Invoice processOrderPayment(Order order) {
        try {
            String receiptId = paymentService.payOrderFromCart(order.getCustomer(), order.getPrice());
            LOG.info("Paiement accepté. Reçu banque : {}", receiptId);
            order.setPaymentReceiptId(receiptId);
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
            Invoice newInvoice = new Invoice(order.getOrderId(), order.getPrice(), receiptId);
            invoiceRepository.save(newInvoice);
            LOG.info("Facture générée : ID {}", newInvoice.getId());
            return newInvoice;

        } catch (PaymentException e) {
            LOG.error("Échec du paiement pour le client {} : {}", order.getCustomer().getName(), e.getMessage());
            order.setStatus(OrderStatus.PAYMENT_FAILED);
            orderRepository.save(order);
            return null;
        }
    }
}
