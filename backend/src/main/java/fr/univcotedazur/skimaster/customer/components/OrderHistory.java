package fr.univcotedazur.skimaster.customer.components;

import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.order.dtos.OrderCreatedDTO;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.exceptions.OrderIdNotFoundException;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderHistory {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderHistory(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public List<OrderCreatedDTO> orderHistory(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerIdNotFoundException(customerId);
        }

        return orderRepository.findAllByCustomerIdWithItems(customerId).stream()
                .map(this::toDto)
                .toList();
    }

    public OrderCreatedDTO getOrder(Long customerId, Long orderId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerIdNotFoundException(customerId);
        }

        Order o = orderRepository.findByCustomerIdAndOrderIdWithItems(customerId, orderId)
                .orElseThrow(() -> new OrderIdNotFoundException(orderId));

        return toDto(o);
    }

    private OrderCreatedDTO toDto(Order o) {
        return new OrderCreatedDTO(
                o.getOrderId(),
                o.getItems(),
                o.getPrice(),
                o.getStatus()
        );
    }

    @Transactional
    public void cancelOrder(Long customerId, Long orderId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerIdNotFoundException(customerId);
        }

        Order order = orderRepository.findByCustomerIdAndOrderIdWithItems(customerId, orderId)
                .orElseThrow(() -> new OrderIdNotFoundException(orderId));

        order.cancel();
        orderRepository.save(order);
    }
}
