package fr.univcotedazur.skimaster.common.controllers;

import fr.univcotedazur.skimaster.common.dto.ErrorDTO;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
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
}
