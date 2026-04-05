package fr.univcotedazur.skimaster.gate.entities;

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

    @NotNull
    private String gatePassedBy;

    public Passage() {}

    public Passage(Long customerId, LocalDateTime occurrenceDate,String gatePassedBy) {
        this.customerId = customerId;
        this.occurrenceDate = occurrenceDate;
        this.gatePassedBy = gatePassedBy;
    }

    public Long getCustomerId() { return customerId; }
    public LocalDateTime getOccurrenceDate() { return occurrenceDate; }
    public String getGatePassedBy() { return gatePassedBy; }
}
