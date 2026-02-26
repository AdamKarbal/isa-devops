package fr.univcotedazur.skimaster.cashier.repositories;

import fr.univcotedazur.skimaster.cashier.entites.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
