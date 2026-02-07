package fr.univcotedazur.skimaster.forfait.entities;

import fr.univcotedazur.skimaster.cart.entities.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Forfait implements Item {

    @NotNull
    private Date validDate;

    @NotNull
    private AgeCategory ageCategory;

    @NotNull
    private PassType passType;

    @Id
    @GeneratedValue
    private Long id;

    private Long idUser;

    public Forfait(Long idUser, Date vDate, AgeCategory aCat, PassType pType){
        this.idUser = idUser;
        this.validDate = vDate;
        this.ageCategory = aCat;
        this.passType = pType;
    }

    public Forfait(){
    }

    private void setIdUser(Long idUser){
        this.idUser = idUser;
    }

    @Override
    public Long getId() { return id;}

    @Override
    public String toString(){
        return "Forfait n°: " + this.id + ".\n"
                + "id user : " + this.idUser + "-" + this.ageCategory + ".\n"
                + "Type de forfait : " + this.passType + ".\n"
                + "Date d'expiration : " + this.validDate + ".\n";

    }

    public String userDisplay(){
        return  "Forfait de type : " + this.passType + ".\n";
    }

}
