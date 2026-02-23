package fr.univcotedazur.skimaster.customer.controllers;

import fr.univcotedazur.skimaster.customer.components.OrderHistory;
import fr.univcotedazur.skimaster.order.dtos.OrderCreatedDTO;
import fr.univcotedazur.skimaster.order.exceptions.OrderIdNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CustomerCareController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class CustomerOrdersController {

    public static final String BASE_URI = "/customers";

    private final OrderHistory orderHistory;

    public CustomerOrdersController(OrderHistory orderHistory) {
        this.orderHistory = orderHistory;
    }

    @GetMapping(path ="/{customerId}/orders")
    public ResponseEntity<List<OrderCreatedDTO>> getOrders(@PathVariable("customerId") Long customerId) throws OrderIdNotFoundException {
        return ResponseEntity.ok(orderHistory.orderHistory(customerId));
    }

    @GetMapping(path ="/{customerId}/orders/{orderId}")
    public ResponseEntity<OrderCreatedDTO> getOrderById(@PathVariable("customerId") Long customerId, @PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(orderHistory.getOrder(customerId, orderId));
    }
}
