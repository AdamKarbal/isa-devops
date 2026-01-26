package fr.univcotedazur.skimaster.common.repositories;

import fr.univcotedazur.skimaster.cashier.connectors.interfaces.Bank;
import fr.univcotedazur.skimaster.cashier.interfaces.Payment;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class NonTransactionalTest { // To show errors that happen when we are not in a transaction

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private Payment payment;


    @Autowired
    private Bank bank;

    private Long orderId;
/*
    @BeforeEach
    @Transactional // setup and tearDown are transactional to make the initialization and cleaning right
    void setup() {
        Customer john = new Customer("john", "1234567890");
        Customer savedJohn= customerRepository.save(john);
        // The order is normally added to the customer in the Cashier component ->  is normal as it is a business
        // component, and linking the two objects is done only if everything goes well in the business process
        savedJohn.addOrder(createdOrder);
    }
*/






}
