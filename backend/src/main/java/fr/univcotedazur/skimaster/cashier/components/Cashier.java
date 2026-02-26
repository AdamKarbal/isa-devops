package fr.univcotedazur.skimaster.cashier.components;

import fr.univcotedazur.skimaster.cashier.connectors.interfaces.Bank;
import fr.univcotedazur.skimaster.cashier.exceptions.PaymentException;
import fr.univcotedazur.skimaster.cashier.interfaces.Payment;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import org.springframework.stereotype.Service;

@Service
public class Cashier implements Payment {

    private final Bank bank;

    public Cashier(Bank bank){
        this.bank = bank;
    }

    @Override
    public String payOrderFromCart(Customer customer, double price) throws PaymentException {
        return bank.pay(customer, price)
                .orElseThrow(() -> new PaymentException(customer.getName(), price));
    }
}
