package fr.univcotedazur.skimaster.customer.components;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.enums.OrderStatus;
import fr.univcotedazur.skimaster.order.exceptions.OrderIdNotFoundException;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderHistoryCancelTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    private OrderHistory orderHistory;

    @BeforeEach
    void setUp() {
        orderHistory = new OrderHistory(orderRepository, customerRepository);
    }

    @Test
    void cancelOrder_whenCustomerDoesNotExist_throwsCustomerIdNotFoundException() {
        Long customerId = 10L;
        Long orderId = 22L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThrows(CustomerIdNotFoundException.class, () -> orderHistory.cancelOrder(customerId, orderId));
    }

    @Test
    void cancelOrder_whenOrderDoesNotExist_throwsOrderIdNotFoundException() {
        Long customerId = 10L;
        Long orderId = 22L;
        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(orderRepository.findByCustomerIdAndOrderIdWithItems(customerId, orderId)).thenReturn(Optional.empty());

        assertThrows(OrderIdNotFoundException.class, () -> orderHistory.cancelOrder(customerId, orderId));
    }

    @Test
    void cancelOrder_whenOrderExists_setsStatusToCanceledAndSaves() {
        Long customerId = 10L;
        Long orderId = 22L;
        Customer customer = new Customer("Alice", "1234567890");
        Order order = new Order(customer, Set.of(), 50.0);

        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(orderRepository.findByCustomerIdAndOrderIdWithItems(customerId, orderId)).thenReturn(Optional.of(order));

        orderHistory.cancelOrder(customerId, orderId);

        assertEquals(OrderStatus.CANCELED, order.getStatus());
        verify(orderRepository).save(order);
    }
}
