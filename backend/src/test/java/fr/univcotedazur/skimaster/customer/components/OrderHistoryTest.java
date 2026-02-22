package fr.univcotedazur.skimaster.customer.components;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.exceptions.OrderIdNotFoundException;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderHistoryTest {

    @Autowired OrderHistory orderHistory;

    @Autowired CustomerRepository customerRepository;
    @Autowired OrderRepository orderRepository;

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

    @Test
    void orderHistory_returnsOrders_forCustomer() {
        Customer c1 = customerRepository.save(new Customer("C1-" + java.util.UUID.randomUUID(), "1111111111"));
        Customer c2 = customerRepository.save(new Customer("C2-" + java.util.UUID.randomUUID(), "2222222222"));

        Order o1 = new Order(c1, 27, OrderStatus.CREATED);
        Order o2 = new Order(c1, 12, OrderStatus.PAID);
        Order o3 = new Order(c2, 99, OrderStatus.CANCELLED);

        orderRepository.save(o1);
        orderRepository.save(o2);
        orderRepository.save(o3);

        List<OrderDTO> res = orderHistory.orderHistory(c1.getId());

        assertEquals(2, res.size());
        // pas d'ordre garanti → check par contenu
        var totals = res.stream().map(OrderDTO::total).sorted().toList();
        assertEquals(List.of(12, 27), totals);

        assertTrue(res.stream().allMatch(d -> d.lines() != null && d.lines().isEmpty()));
    }

    @Test
    void getOrder_returnsOneOrder_forCustomer() {
        Customer c = customerRepository.save(new Customer("C-" + java.util.UUID.randomUUID(), "1234567890"));

        Order o = new Order(c, 27, OrderStatus.CREATED);
        o = orderRepository.save(o);

        OrderDTO dto = orderHistory.getOrder(c.getId(), o.getId());

        assertEquals(o.getId(), dto.id());
        assertEquals(OrderStatus.CREATED, dto.status());
        assertEquals(27, dto.total());
        assertNotNull(dto.lines());
        assertTrue(dto.lines().isEmpty());
    }
}
