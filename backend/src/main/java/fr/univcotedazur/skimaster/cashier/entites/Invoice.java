package fr.univcotedazur.skimaster.cashier.entites;

import fr.univcotedazur.skimaster.order.entities.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Invoice {
    @Id
    @GeneratedValue
    private Long id;

    private Long orderRefId;
    private double amount;
    private LocalDateTime paymentDate;
    private String bankReceiptId;

    public Invoice() {
    }
    public Invoice(Long orderRefId, double amount, String bankReceiptId) {
        this.orderRefId = orderRefId;
        this.amount = amount;
        this.bankReceiptId = bankReceiptId;
        this.paymentDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getBankReceiptId() {
        return bankReceiptId;
    }

    public double getAmount() {
        return amount;
    }
}
