package fr.univcotedazur.skimaster.cart.entities;

import fr.univcotedazur.skimaster.cart.interfaces.Item;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.ArrayList;
import java.util.List;
@Entity
public class Product {
    @ManyToMany
    private List<Forfait> packageItems = new ArrayList<>();
    @Id
    @GeneratedValue
    long productId;
    @Min(1)
    @Max(100)
    int reduction=1;
    public Product() {
    }

    public Product(List<Forfait> items) {
        this.packageItems = new ArrayList<>(items);
    }

    public Product(List<Forfait> items, int reduction){
        this.packageItems = new ArrayList<>(items);
        this.reduction = reduction;
    }

    public void addItem(Forfait item) {
        packageItems.add(item);
    }

    public void removeItem(Forfait item) {
        packageItems.remove(item);
    }

    public List<Forfait> getItems() { return packageItems; }
    public Long getId() { return productId; }
    public int getReduction(){
        return reduction;
    }
}
