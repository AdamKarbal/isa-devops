package fr.univcotedazur.skimaster.order.entities;

import jakarta.persistence.*;

@Entity
public class OrderLine {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", nullable=false)
    private Order order;

    @Column(name="product_code", nullable=false)
    private String productCode;

    @Column(nullable=false)
    private int quantity;

    @Column(name="unit_price", nullable=false)
    private int unitPrice;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public OrderLine(){

    }

    public OrderLine(Order order, String productCode, int quantity, int unitPrice) {
        this.order = order;
        this.productCode = productCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
