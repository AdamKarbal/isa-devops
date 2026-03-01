package fr.univcotedazur.skimaster.cli.model;

import java.util.ArrayList;
import java.util.List;

public class CliProduct {

    private Long id;
    private List<Long> forfaitOfferIds = new ArrayList<>();
    private int reduction;

    public CliProduct() {
    }

    public CliProduct(List<Long> forfaitOfferIds, int reduction) {
        this.forfaitOfferIds = new ArrayList<>(forfaitOfferIds);
        this.reduction = reduction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getForfaitOfferIds() {
        return forfaitOfferIds;
    }

    public void setForfaitOfferIds(List<Long> forfaitOfferIds) {
        this.forfaitOfferIds = forfaitOfferIds;
    }

    public int getReduction() {
        return reduction;
    }

    public void setReduction(int reduction) {
        this.reduction = reduction;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", forfaitOfferIds=" + forfaitOfferIds +
                ", reduction=" + reduction +
                '}';
    }
}
