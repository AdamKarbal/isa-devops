package fr.univcotedazur.skimaster.cart.components;

import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerFinder;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cart.interfaces.CartModifier;
import fr.univcotedazur.skimaster.cart.interfaces.CartProcessor;
import fr.univcotedazur.skimaster.cart.interfaces.Item;
import fr.univcotedazur.skimaster.order.interfaces.OrderCreator;
import org.springframework.stereotype.Component;

@Component
public class CartHandler implements CartProcessor, CartModifier {
    private final OrderCreator orderCreator;
    private final CustomerFinder customerFinder;
    public CartHandler(OrderCreator orderCreator,CustomerFinder customerFinder){

        this.orderCreator = orderCreator;
        this.customerFinder = customerFinder;
    }
    private double priceProduct(Product product) {
        double itemsPrice = product.getItems().stream()
                .mapToDouble(Item::getPrice)
                .sum();

        return itemsPrice / product.getReduction();
    }

    public double computeCartPrice(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(this::priceProduct)
                .sum();
    }

    public Order validate(Cart cart){
        return null;
    }

    public void addItem(Long customerId,Product p,int quantity) throws CustomerIdNotFoundException {
        Customer current = customerFinder.retrieveCustomer(customerId);
        Cart cart = current.getCart();
        cart.addItem(p);
    }

    public void removeItem(Cart cart,Product p) {

    }
}
