package fr.univcotedazur.skimaster.order.repositories;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.order.OrderStatus;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.entities.OrderLine;
import fr.univcotedazur.skimaster.product.entities.Product;
import fr.univcotedazur.skimaster.product.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    private static final String CC_OK = "1234567890";

    @Autowired OrderRepository orderRepository;
    @Autowired CustomerRepository customerRepository;
    @Autowired ProductRepository productRepository;

    @Autowired TestEntityManager em;

    @Test
    void findAllByCustomerIdWithLines_filtersAndFetchesLines_orderedDesc() {
        Customer c1 = customerRepository.save(new Customer("C1-" + java.util.UUID.randomUUID(), CC_OK));
        Customer c2 = customerRepository.save(new Customer("C2-" + java.util.UUID.randomUUID(), CC_OK));

        Product p = productRepository.save(new Product(10, PassType.FULL, AgeCategory.ADULT, new Date()));

        Order o1 = new Order(c1, 27, OrderStatus.CREATED);
        o1.addLine(new OrderLine(o1, p, 2, 10));
        orderRepository.save(o1);

        Order o2 = new Order(c1, 12, OrderStatus.PAID);
        o2.addLine(new OrderLine(o2, p, 1, 12));
        orderRepository.save(o2);

        Order o3 = new Order(c2, 99, OrderStatus.CANCELLED);
        o3.addLine(new OrderLine(o3, p, 3, 33));
        orderRepository.save(o3);

        em.flush();
        em.clear();

        List<Order> res = orderRepository.findAllByCustomerIdWithLines(c1.getId());

        assertEquals(2, res.size());

        assertTrue(res.get(0).getId() > res.get(1).getId());

        assertEquals(1, res.get(0).getLines().size());
        assertEquals(1, res.get(1).getLines().size());
    }

    @Test
    void findByCustomerIdAndIdWithLines_returnsEmpty_ifOrderBelongsToOtherCustomer() {
        Customer c1 = customerRepository.save(new Customer("C1-" + java.util.UUID.randomUUID(), CC_OK));
        Customer c2 = customerRepository.save(new Customer("C2-" + java.util.UUID.randomUUID(), CC_OK));

        Product p = productRepository.save(new Product(10, PassType.FULL, AgeCategory.ADULT, new Date()));

        Order o = new Order(c2, 99, OrderStatus.CREATED);
        o.addLine(new OrderLine(o, p, 1, 10));
        o = orderRepository.save(o);

        em.flush();
        em.clear();

        assertTrue(orderRepository.findByCustomerIdAndIdWithLines(c1.getId(), o.getId()).isEmpty());
    }

    @Test
    void findByCustomerIdAndIdWithLines_fetchesLines() {
        Customer c = customerRepository.save(new Customer("C-" + java.util.UUID.randomUUID(), CC_OK));
        Product p = productRepository.save(new Product(7, PassType.FULL, AgeCategory.ADULT, new Date()));

        Order o = new Order(c, 14, OrderStatus.CREATED);
        o.addLine(new OrderLine(o, p, 2, 7));
        o = orderRepository.save(o);

        em.flush();
        em.clear();

        Order fetched = orderRepository.findByCustomerIdAndIdWithLines(c.getId(), o.getId()).orElseThrow();

        assertEquals(1, fetched.getLines().size());
        OrderLine line = fetched.getLines().iterator().next();
        assertEquals(2, line.getQuantity());
        assertEquals(7, line.getUnitPrice());
    }
}
