package fr.univcotedazur.skimaster.cli.model;

public class CliForfaitOffer {

    private Long id;
    private String ageCategory;
    private String passType;
    private double price;

    public CliForfaitOffer() {
    }

    public CliForfaitOffer(String ageCategory, String passType, double price) {
        this.ageCategory = ageCategory;
        this.passType = passType;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(String ageCategory) {
        this.ageCategory = ageCategory;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ForfaitOffer{" +
                "id=" + id +
                ", ageCategory='" + ageCategory + '\'' +
                ", passType='" + passType + '\'' +
                ", price=" + price +
                '}';
    }
}
