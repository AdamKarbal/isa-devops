package fr.univcotedazur.skimaster.forfait.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Objects;

@Entity
@Table(name = "forfait_offer")
public class ForfaitOffer {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AgeCategory ageCategory;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PassType passType;

    @NotNull
    @Positive
    private double price;


    protected ForfaitOffer() {}

    public ForfaitOffer(AgeCategory ageCategory, PassType passType, double price) {
        this.ageCategory = ageCategory;
        this.passType = passType;
        this.price = price;
    }

    public Long getId() { return id; }
    public AgeCategory getAgeCategory() { return ageCategory; }
    public PassType getPassType() { return passType; }
    public double getPrice() { return price; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForfaitOffer other)) return false;
        return id != null && Objects.equals(id, other.id);
    }

    @Override public int hashCode() { return 31; }
}
