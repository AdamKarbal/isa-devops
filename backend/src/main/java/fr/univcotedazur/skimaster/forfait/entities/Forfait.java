package fr.univcotedazur.skimaster.forfait.entities;

import fr.univcotedazur.skimaster.cart.entities.Item;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Forfait {
    @Enumerated(EnumType.STRING)
    @NotNull
    private AgeCategory ageCategory;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PassType passType;

    private LocalDate validDate;
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @NotNull
    private Long userId;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offer_id", nullable = false)
    private ForfaitOffer offer;

    public Forfait(Customer customer, LocalDate vDate, ForfaitOffer offer){
        this.customer = customer;
        this.validDate = vDate;
        this.offer = offer;
    }

    public Forfait(){
    }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public AgeCategory getAgeCategory(){
        return offer.getAgeCategory();
    }


    public Long getId() { return id;}

    @Override
    public String toString(){
        return "Forfait n°: " + this.id + ".\n"
                + "id user : " + this.customer.getId() + "-" + this.getAgeCategory() + ".\n"
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
        System.out.println(this.id);
        System.out.println(forfait.id);
        return Objects.equals(id, forfait.id);
    }

    @Override public int hashCode(){
        return Objects.hash(id);
    }
}
