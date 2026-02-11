package fr.univcotedazur.skimaster.order.entities;

import fr.univcotedazur.skimaster.product.entities.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "OrderLine", indexes = {})
public class OrderLine {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="Order_id", nullable=false)
    private Order order;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "Product", nullable = false)
    private Product product;

    @Column(nullable=false)
    private int quantity;

    @Column(name="Unit_price", nullable=false)
    private int unitPrice;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public OrderLine(){

    }

    public OrderLine(Order order, Product product, int quantity, int unitPrice) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Product getProductCode() { return product; }
    public int getQuantity() { return quantity; }
    public int getUnitPrice() { return unitPrice; }

}
