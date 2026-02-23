package fr.univcotedazur.skimaster.order.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderIdNotFoundException extends Exception {

    private final Long orderId;

    public OrderIdNotFoundException(Long orderId) {
        super("Order with ID " + orderId + " was not found.");
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
