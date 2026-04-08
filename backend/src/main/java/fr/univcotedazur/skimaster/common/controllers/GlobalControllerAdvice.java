package fr.univcotedazur.skimaster.common.controllers;

import fr.univcotedazur.skimaster.admin.exceptions.StillUnresolvedIncident;
import fr.univcotedazur.skimaster.cashier.exceptions.PaymentException;
import fr.univcotedazur.skimaster.common.dto.ErrorDTO;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice(assignableTypes = {CustomerCareController.class, CartController.class})

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler({CustomerIdNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(CustomerIdNotFoundException e) {
        return new ErrorDTO("Customer not found", e.getId() + " is not a valid customer Id");
    }

    @ExceptionHandler({StillUnresolvedIncident.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleStillUnresolvedIncident(StillUnresolvedIncident e) {
        return new ErrorDTO("Incident non resolu", e.getMessage());
    }

}
