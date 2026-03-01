package fr.univcotedazur.skimaster.pass.components;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import fr.univcotedazur.skimaster.pass.connectors.TurnstileProxy;
import fr.univcotedazur.skimaster.pass.repositories.PassageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional 
public class PassServiceTest {

    @Autowired
    private PassService passService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PassageRepository passageRepository;

    @MockitoBean
    private TurnstileProxy turnstileProxy;

    private Customer superJulien;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
        superJulien = new Customer("SuperJulien", "1238969831");
        superJulien.setHasSuperCarte(true);
        superJulien = customerRepository.save(superJulien);
    }

    @Test
    void testSuperCarteBillingOnPassage() {
        passService.processReceivedPassages(List.of(superJulien.getId()));
        assertEquals(1, passageRepository.findAll().size());
        List<Order> orders = orderRepository.findAllByCustomerIdWithItems(superJulien.getId());
        assertEquals(1, orders.size());
        assertEquals(15.0, orders.get(0).getPrice());
    }

    @Test
    void testIdempotencySuperCarte() {
        passService.processReceivedPassages(List.of(superJulien.getId()));
        passService.processReceivedPassages(List.of(superJulien.getId()));
        assertEquals(2, passageRepository.findAll().size());
        List<Order> orders = orderRepository.findAllByCustomerIdWithItems(superJulien.getId());
        assertEquals(1, orders.size());
    }
}
