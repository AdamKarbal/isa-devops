package fr.univcotedazur.skimaster.customer.entities;


import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.order.entities.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.aspectj.weaver.ast.Or;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id; // Whether Long/Int or UUID are better primary keys, exposable outside is a vast issue, keep it simple here

    @NotBlank
    @Column(unique = true)
    private String name;

    @Pattern(regexp = "\\d{10}+", message = "Invalid creditCardNumber")
    private String creditCard;

    @OneToOne
    private Cart cart = new Cart();

    @OneToMany(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "customer")
    private Set<Order> orders = new HashSet<>();

    public Customer() {
    }

    public Customer(String n, String c) {
        this.name = n;
        this.creditCard = c;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public Cart getCart(){
        return cart;
    }

    public void setCart(Cart cart) {
         this.cart=cart;
    }

    public void clearCart(){
        this.setCart(new Cart());
    }

    public void addOrder(Order order){
        this.orders.add(order);
    }
    public void setOrders(Set<Order> orders){
        this.orders=orders;
    }
    public Set<Order> getOrders(){
        return orders;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(name, customer.name) && Objects.equals(creditCard, customer.creditCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, creditCard);
    }

}
