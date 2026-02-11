package fr.univcotedazur.skimaster.product.entities;

import fr.univcotedazur.skimaster.cart.entities.Item;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "Product", indexes = {})
public class Product implements Item {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PassType passType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AgeCategory ageCategory;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date validDate;

    public Product() {
    }

    public Product(int price, PassType passType, AgeCategory ageCategory, Date validDate) {
        this.price = price;
        this.passType = passType;
        this.ageCategory = ageCategory;
        this.validDate = validDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }
}
