package fr.univcotedazur.skimaster.cashier.components;

import fr.univcotedazur.skimaster.cashier.entites.Invoice;
import fr.univcotedazur.skimaster.cashier.exceptions.PaymentException;
import fr.univcotedazur.skimaster.cashier.interfaces.Payment;
import fr.univcotedazur.skimaster.cashier.repositories.InvoiceRepository;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.enums.OrderStatus;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"gate.host.baseurl=http://localhost:8082"})
@Transactional
class InvoiceHandlerTest {

    @Autowired
    private InvoiceHandler invoiceHandler;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @MockitoBean
    private Payment paymentMock;

    private Customer julien;
    private Order testOrder;

    @BeforeEach
    void setUpContext() {
        julien = new Customer("Julien", "1234567890");
        julien = customerRepository.save(julien);
        testOrder = new Order(julien, new HashSet<>(), 50.0);
        testOrder = orderRepository.save(testOrder);
    }

    @Test
    void processOrderPaymentSuccessTest() throws PaymentException {
        when(paymentMock.payOrderFromCart(eq(julien), anyDouble())).thenReturn("BANK-REF-999");
        Invoice result = invoiceHandler.processOrderPayment(testOrder);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("BANK-REF-999", result.getBankReceiptId());
        Order updatedOrder = orderRepository.findById(testOrder.getOrderId()).orElseThrow();
        Assertions.assertEquals(OrderStatus.PAID, updatedOrder.getStatus());
        Assertions.assertEquals("BANK-REF-999", updatedOrder.getPaymentReceiptId());
        long invoiceCount = invoiceRepository.count();
        Assertions.assertEquals(1, invoiceCount);
    }

    @Test
    void processOrderPaymentFailureTest() throws PaymentException {
        when(paymentMock.payOrderFromCart(eq(julien), anyDouble()))
                .thenThrow(new PaymentException("Julien", 50.0));
        Invoice result = invoiceHandler.processOrderPayment(testOrder);
        Assertions.assertNull(result);
        Order updatedOrder = orderRepository.findById(testOrder.getOrderId()).orElseThrow();
        Assertions.assertEquals(OrderStatus.PAYMENT_FAILED, updatedOrder.getStatus());
        long invoiceCount = invoiceRepository.count();
        Assertions.assertEquals(0, invoiceCount);
    }
}
