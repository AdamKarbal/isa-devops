package fr.univcotedazur.skimaster.cashier.components;

import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cashier.entites.Invoice;
import fr.univcotedazur.skimaster.cashier.exceptions.PaymentException;
import fr.univcotedazur.skimaster.cashier.interfaces.InvoiceCreator;
import fr.univcotedazur.skimaster.cashier.interfaces.InvoiceFinder;
import fr.univcotedazur.skimaster.cashier.interfaces.Payment;
import fr.univcotedazur.skimaster.cashier.repositories.InvoiceRepository;
import fr.univcotedazur.skimaster.common.helpers.Converter;
import fr.univcotedazur.skimaster.forfait.components.ForfaitRegistry;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.enums.OrderStatus;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class InvoiceHandler implements InvoiceCreator, InvoiceFinder {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceHandler.class);
    private final Payment paymentService;
    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final ForfaitRegistry forfaitRegistry;

    public InvoiceHandler(Payment paymentService, OrderRepository orderRepository, InvoiceRepository invoiceRepository,ForfaitRegistry forfaitRegistry) {
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.forfaitRegistry = forfaitRegistry;
    }

    @Override
    @Transactional
    public Invoice processOrderPayment(Order order) {
        try {
            order = loadOrderWithItems(order);
            String receiptId = paymentService.payOrderFromCart(order.getCustomer(), order.getPrice());
            LOG.info("Paiement accepté. Reçu banque : {}", receiptId);
            order.setPaymentReceiptId(receiptId);
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
            createForfaitsFromPaidOrder(order);
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

    private void createForfaitsFromPaidOrder(Order order) {
        Set<Product> products = order.getItems();
        if (products == null || products.isEmpty()) {
            return;
        }

        List<Forfait> createdForfaits = new ArrayList<>();
        for (Product product : products) {
            List<ForfeitOffer> offers = product.getItems();
            if (offers == null || offers.isEmpty()) {
                continue;
            }

            for (ForfeitOffer offer : offers) {
                Forfait forfait = new Forfait(order.getCustomer().getId(), Converter.computeValidDate(offer.getPassType()), offer);
                createdForfaits.add(forfait);
            }
        }

        if (!createdForfaits.isEmpty()) {
            forfaitRegistry.saveAll(createdForfaits);
            LOG.info("{} forfait(s) créés pour le client {} après paiement de la commande {}",
                    createdForfaits.size(), order.getCustomer().getId(), order.getOrderId());
        }
    }

    private Order loadOrderWithItems(Order order) {
        if (order.getOrderId() == null) {
            return order;
        }
        return orderRepository.findByIdWithItems(order.getOrderId())
                .orElse(order);
    }

    @Override
    public List<Invoice> findInvoicesByCustomerId(Long customerId) {
        return invoiceRepository.findAll().stream()
                .filter(i -> {
                    Optional<Order> o = orderRepository.findById(i.getOrderRefId());
                    return o.isPresent() && o.get().getCustomer().getId().equals(customerId);
                })
                .toList();
    }
}
