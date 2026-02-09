package fr.univcotedazur.skimaster.forfait.entities;

import fr.univcotedazur.skimaster.cart.entities.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
public class Forfait implements Item {

    @NotNull
    private LocalDate validDate;

    @NotNull
    private AgeCategory ageCategory;

    @NotNull
    private PassType passType;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Long userId;

    public Forfait(Long userId, LocalDate vDate, AgeCategory aCat, PassType pType){
        this.userId = userId;
        this.validDate = vDate;
        this.ageCategory = aCat;
        this.passType = pType;
    }

    public Forfait(){
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public Long getUserId(){
        return this.userId;
    }

    @Override
    public Long getId() { return id;}

    @Override
    public String toString(){
        return "Forfait n°: " + this.id + ".\n"
                + "id user : " + this.userId + "-" + this.ageCategory + ".\n"
                + "Type de forfait : " + this.passType + ".\n"
                + "Date d'expiration : " + this.validDate + ".\n";

    }

    public String userDisplay(){
        return  "Forfait de type : " + this.passType + ".\n";
    }

    public @NotNull LocalDate getValidDate() {
        return validDate;
    }

    public @NotNull AgeCategory getAgeCategory() {
        return ageCategory;
    }

    public @NotNull PassType getPassType() {
        return passType;
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
