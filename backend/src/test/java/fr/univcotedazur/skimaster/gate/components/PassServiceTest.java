package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import fr.univcotedazur.skimaster.gate.repositories.PassageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional 
public class PassServiceTest {


    private static final String GATE_ID = "gate-1";
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
        passService.processReceivedPassages(List.of(new Passage(superJulien.getId(), LocalDateTime.now(),GATE_ID)));
        assertEquals(1, passageRepository.findAll().size());
        List<Order> orders = orderRepository.findAllByCustomerIdWithItems(superJulien.getId());
        assertEquals(1, orders.size());
        assertEquals(15.0, orders.getFirst().getPrice());
    }

    @Test
    void testIdempotencySuperCarte() {
        passService.processReceivedPassages(List.of(new Passage(superJulien.getId(), LocalDateTime.now(),GATE_ID)));
        passService.processReceivedPassages(List.of(new Passage(superJulien.getId(), LocalDateTime.now(),GATE_ID)));
        assertEquals(2, passageRepository.findAll().size());
        List<Order> orders = orderRepository.findAllByCustomerIdWithItems(superJulien.getId());
        assertEquals(1, orders.size());
    }
}
