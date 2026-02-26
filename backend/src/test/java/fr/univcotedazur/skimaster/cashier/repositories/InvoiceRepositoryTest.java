package fr.univcotedazur.skimaster.cashier.repositories;

import fr.univcotedazur.skimaster.cashier.entites.Invoice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    void testIdGenerationAndSave() {
        Invoice invoice = new Invoice(99L, 150.0, "BANK-RECEIPT-123");
        Assertions.assertNull(invoice.getId());
        invoiceRepository.saveAndFlush(invoice);
        Assertions.assertNotNull(invoice.getId());
        Invoice retrieved = invoiceRepository.findById(invoice.getId()).orElse(null);
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals("BANK-RECEIPT-123", retrieved.getBankReceiptId());
    }
}
