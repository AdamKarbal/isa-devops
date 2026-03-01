package fr.univcotedazur.skimaster.cashier.components;

import fr.univcotedazur.skimaster.cashier.connectors.interfaces.Bank;
import fr.univcotedazur.skimaster.cashier.exceptions.PaymentException;
import fr.univcotedazur.skimaster.cashier.interfaces.Payment;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"gate.host.baseurl=http://localhost:8082"})
@Transactional
class CashierTest {

    @Autowired
    private Payment cashier;

    @MockitoBean
    private Bank bankMock;

    private Customer john;
    private Customer pat;

    @BeforeEach
    void setUpContext() {
        john = new Customer("John", "1234896983");
        pat = new Customer("Pat", "1234567890");
        when(bankMock.pay(eq(john), anyDouble())).thenReturn(Optional.of("RECEIPT-OK-123"));
        when(bankMock.pay(eq(pat), anyDouble())).thenReturn(Optional.empty());
    }

    @Test
    void testSuccessfulPayment() throws PaymentException {
        String receipt = cashier.payOrderFromCart(john, 100.0);
        Assertions.assertEquals("RECEIPT-OK-123", receipt);
    }

    @Test
    void testPaymentError() {
        Assertions.assertThrows(PaymentException.class, () -> {
            cashier.payOrderFromCart(pat, 44.2);
        });
    }
}
