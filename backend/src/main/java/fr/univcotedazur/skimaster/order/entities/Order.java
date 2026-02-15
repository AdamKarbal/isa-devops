package fr.univcotedazur.skimaster.order.entities;

import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.order.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

@Entity
@Table(name= "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long orderId;
    @ElementCollection
    private Set<Product> items;
    @Positive
    private double price;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus status;
    public Order(){
    }

    public Order(Customer customer,Set<Product> items,double price){
        this.customer=customer;
        this.items = items;
        this.price = price;
        this.status = OrderStatus.COMPLETED;
        this.customer.addOrder(this);
    }

    public Long getOrderId(){
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Set<Product> getItems(){
        return items;
    }
    public double getPrice(){
        return price;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setItems(Set<Product> cartItems){
        this.items = cartItems;
    }
    public void setPrice(double price){
        this.price = price;
    }

}
