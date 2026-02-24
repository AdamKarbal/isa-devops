package fr.univcotedazur.skimaster.forfait.entities;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offer_id", nullable = false)
    private ForfaitOffer offer;

    public Forfait(Long customerId, LocalDate vDate, ForfaitOffer offer) {
        this.customerId = customerId;
        this.validDate = vDate;
        this.offer = offer;
    }

    public Forfait(Customer customer, LocalDate vDate, ForfaitOffer offer) {
        this(customer.getId(), vDate, offer);
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
        return id != null && id.equals(forfait.id);
    }

    @Override public int hashCode(){
        return 31;
    }
}
