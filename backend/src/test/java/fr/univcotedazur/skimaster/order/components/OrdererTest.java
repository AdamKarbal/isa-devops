package fr.univcotedazur.skimaster.order.components;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.factories.OrderFactory;
import fr.univcotedazur.skimaster.order.interfaces.OrderCreator;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@SpringBootTest
@Transactional
class OrdererTest {

    @Autowired
    private OrderCreator orderer;

    @Test
    @Transactional
    void createOrder_persists_and_generates_id() {
        Customer customer = new Customer("john", "1234567890");

        Order created = orderer.createOrder(customer);
        assertThat(created.getOrderId()).isNotNull();
        assertThat(created.getItems().size()==customer.getCart().getItems().size());
        assertThat(customer.getOrders().contains(created));
    }
}
