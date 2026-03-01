package fr.univcotedazur.skimaster.customer.components;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.enums.OrderStatus;
import fr.univcotedazur.skimaster.order.exceptions.OrderIdNotFoundException;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"gate.host.baseurl=http://localhost:8082"})

@Transactional
class OrderHistoryTest {

    @Autowired OrderHistory orderHistory;

    @Autowired CustomerRepository customerRepository;

    @Test
    void orderHistory_customerNotFound_throws() {
        assertThrows(CustomerIdNotFoundException.class, () -> orderHistory.orderHistory(999999L));
    }

    @Test
    void getOrder_customerNotFound_throws() {
        assertThrows(CustomerIdNotFoundException.class, () -> orderHistory.getOrder(999999L, 1L));
    }

    @Test
    void getOrder_orderNotFoundForCustomer_throws() {
        Customer c = customerRepository.save(new Customer("C-" + java.util.UUID.randomUUID(), "1234567891"));
        assertThrows(OrderIdNotFoundException.class, () -> orderHistory.getOrder(c.getId(), 999999L));
    }
}
