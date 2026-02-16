package fr.univcotedazur.skimaster.cashier.interfaces;

import fr.univcotedazur.skimaster.cashier.exceptions.PaymentException;
import fr.univcotedazur.skimaster.customer.entities.Customer;

public interface Payment {

     String payOrderFromCart(Customer customer, double price) throws PaymentException;

}
