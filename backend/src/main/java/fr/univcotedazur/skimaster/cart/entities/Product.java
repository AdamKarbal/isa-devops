package fr.univcotedazur.skimaster.cart.entities;

import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.ArrayList;
import java.util.List;
@Entity
public class Product {
    @ManyToMany(fetch = FetchType.EAGER)
    private List<ForfeitOffer> packageItems = new ArrayList<>();
    @Id
    @GeneratedValue
    private Long productId;
    @Min(1)
    @Max(100)
    int reduction=1;
    public Product() {
    }

    public Product(List<ForfeitOffer> items) {
        this.packageItems = new ArrayList<>(items);
    }

    public Product(List<ForfeitOffer> items, int reduction){
        this.packageItems = new ArrayList<>(items);
        this.reduction = reduction;
    }

    public void addItem(ForfeitOffer item) {
        packageItems.add(item);
    }

    public void removeItem(Forfait item) {
        packageItems.remove(item);
    }

    public List<ForfeitOffer> getItems() { return packageItems; }
    public Long getId() { return productId; }
    public int getReduction(){
        return reduction;
    }
}
