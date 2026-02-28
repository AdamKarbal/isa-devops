package fr.univcotedazur.skimaster.common.controllers;

import fr.univcotedazur.skimaster.common.dto.ErrorDTO;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.forfait.exceptions.ForfaitIdNotFoundException;
import fr.univcotedazur.skimaster.order.exceptions.OrderIdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerIdNotFoundException.class)
    public ErrorDTO handleCustomerNotFound(CustomerIdNotFoundException e) {
        return new ErrorDTO("client introuvable", "customer_id=" + e.getId());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ForfaitIdNotFoundException.class)
    public ErrorDTO handleForfaitNotFound(ForfaitIdNotFoundException e) {
        return new ErrorDTO("forfait introuvable", "forfait_id=" + e.getId());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderIdNotFoundException.class)
    public ErrorDTO handleOrderNotFound(OrderIdNotFoundException e) {
        return new ErrorDTO("commande introuvable", "order_id=" + e.getId());
    }
}
