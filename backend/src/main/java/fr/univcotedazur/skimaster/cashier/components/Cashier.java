package fr.univcotedazur.skimaster.cashier.components;

import fr.univcotedazur.skimaster.cashier.connectors.interfaces.Bank;
import fr.univcotedazur.skimaster.cashier.exceptions.PaymentException;
import fr.univcotedazur.skimaster.cashier.interfaces.Payment;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.gate.components.PassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Cashier implements Payment {
    private static final Logger LOG = LoggerFactory.getLogger(Cashier.class);

    private final Bank bank;

    public Cashier(Bank bank){
        this.bank = bank;
    }

    @Override
    public String payOrderFromCart(Customer customer, double price) throws PaymentException {
        LOG.info("[SYSTEME-EXTERNE][Banque][CASHIER] Requête de paiement de {}€ pour le client : {}", price, customer.getName());
        return bank.pay(customer, price)
                .map(receipt -> {
                    LOG.info("[SYSTEME-EXTERNE][Banque][CASHIER] Paiement APPROUVÉ. Reçu n° {}", receipt);
                    return receipt;
                })
                .orElseThrow(() -> {
                    LOG.error("[SYSTEME-EXTERNE][Banque][CASHIER] Paiement REFUSÉ pour le client : {}", customer.getName());
                    return new PaymentException(customer.getName(), price);
                });
    }
}
