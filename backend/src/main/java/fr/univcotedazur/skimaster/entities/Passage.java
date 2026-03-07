package fr.univcotedazur.skimaster.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Passage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long customerId;

    @NotNull
    private LocalDateTime occurrenceDate;

    public Passage() {}

    public Passage(Long customerId, LocalDateTime occurrenceDate) {
        this.customerId = customerId;
        this.occurrenceDate = occurrenceDate;
    }

    public Long getCustomerId() { return customerId; }
    public LocalDateTime getOccurrenceDate() { return occurrenceDate; }
}
