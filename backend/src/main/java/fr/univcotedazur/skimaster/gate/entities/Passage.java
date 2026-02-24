package fr.univcotedazur.skimaster.gate.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "passages")
public class Passage {
    @Id
    @GeneratedValue()
    private Long id;

    @NotNull
    private Long customerId;

    @NotNull
    @ManyToOne
    private Gate gate;

    @NotNull
    private int customerAge;

    @NotNull
    private LocalDateTime date;

    public Passage() {

    }

    public Passage(Long id, Long customerId, int customerAge, LocalDateTime date, Gate gate) {
        this.id = id;
        this.customerId = customerId;
        this.customerAge = customerAge;
        this.date = date;
        this.gate = gate;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Gate getGate() {
        return gate;
    }

    public int getCustomerAge() {
        return customerAge;
    }
}
