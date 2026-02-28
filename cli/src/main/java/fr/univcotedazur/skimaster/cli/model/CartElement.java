package fr.univcotedazur.skimaster.cli.model;

public class CartElement {
    private Long productId;
    private int quantity;
    public CartElement(){}
    public CartElement(Long productId,int quantity){
        this.productId=productId;
        this.quantity=quantity;
    }

    @Override
    public String toString() {
        return quantity + " of : "+productId;
    }

    public Long getProductId(){
        return productId;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setProductId(Long productId){
        this.productId=productId;
    }
    public void setQuantity(int quantity){
        this.quantity=quantity;
    }
}
