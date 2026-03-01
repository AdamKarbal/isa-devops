package fr.univcotedazur.skimaster.order.controllers;

import fr.univcotedazur.skimaster.cashier.entites.Invoice;
import fr.univcotedazur.skimaster.cashier.exceptions.PaymentException;
import fr.univcotedazur.skimaster.cashier.interfaces.InvoiceCreator;
import fr.univcotedazur.skimaster.order.dtos.OrderCreatedDTO;
import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.enums.OrderStatus;
import fr.univcotedazur.skimaster.order.exceptions.OrderIdNotFoundException;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = OrderController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class OrderController {

    public static final String BASE_URI = "/orders";

    private final OrderRepository orderRepository;
    private final InvoiceCreator invoiceHandler;

    public OrderController(OrderRepository orderRepository, InvoiceCreator invoiceHandler) {
        this.orderRepository = orderRepository;
        this.invoiceHandler = invoiceHandler;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<OrderCreatedDTO>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders.stream()
                .map(o -> new OrderCreatedDTO(
                        o.getOrderId(),
                        o.getItems(),
                        o.getPrice()))
                .toList());
    }

    @GetMapping(path = "/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable("orderId") Long orderId) throws OrderIdNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderIdNotFoundException(orderId));
        return ResponseEntity.ok(order);
    }
    @PostMapping(path = "/{orderId}/pay")
    public ResponseEntity<Invoice> payOrder(@PathVariable("orderId") Long orderId)
            throws OrderIdNotFoundException, PaymentException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderIdNotFoundException(orderId));
        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.COMPLETED) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Invoice invoice = invoiceHandler.processOrderPayment(order);
        if (invoice == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(invoice);
    }
}
