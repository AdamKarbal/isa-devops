package fr.univcotedazur.skimaster.forfait.entities;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "forfait", indexes = {
        @Index(name = "idx_forfait_customer_id", columnList = "customer_id")
})
public class Forfait {
    @NotNull
    private LocalDate validDate;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column (name = "beeper")
    private BeepSignal beep;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "offer_id", nullable = false)
    private ForfeitOffer offer;

    public Forfait(Long customerId, LocalDate vDate, ForfeitOffer offer) {
        this.customerId = customerId;
        this.validDate = vDate;
        this.offer = offer;
        this.beep=BeepSignal.ACCESS_DENIED;
    }

    public Forfait(Customer customer, LocalDate vDate, ForfeitOffer offer) {
        this(customer.getId(), vDate, offer);
    }

    public BeepSignal getBeep(){
        return this.beep;
    }

    public void setBeep(BeepSignal beep){
        this.beep=beep;
    }
    public Forfait() {}

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public AgeCategory getAgeCategory(){
        return offer.getAgeCategory();
    }

    public Long getId() { return id;}

    @Override
    public String toString(){
        return "Forfait n°: " + this.id + ".\n"
                + "id user : " + this.customerId + "-" + this.getAgeCategory() + ".\n"
                + "Type de forfait : " + this.getPassType() + ".\n"
                + "Date d'expiration : " + this.validDate + ".\n";

    }

    public String userDisplay(){
        return  "Forfait de type : " + this.getPassType() + ".\n";
    }

    public @NotNull LocalDate getValidDate() {
        return validDate;
    }

    public @NotNull PassType getPassType() {
        return offer.getPassType();
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Forfait forfait = (Forfait) o;
        return id != null && Objects.equals(id, forfait.id);
    }

    @Override public int hashCode(){
        return Objects.hash(id);
    }
}
