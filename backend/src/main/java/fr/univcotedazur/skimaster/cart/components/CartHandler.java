package fr.univcotedazur.skimaster.cart.components;

import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.cart.exceptions.EmptyCartException;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerFinder;
import fr.univcotedazur.skimaster.forfait.entities.ForfaitOffer;
import fr.univcotedazur.skimaster.order.components.Orderer;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cart.interfaces.CartModifier;
import fr.univcotedazur.skimaster.cart.interfaces.CartProcessor;
import org.springframework.stereotype.Component;

@Component
public class CartHandler implements CartProcessor, CartModifier {
    private final Orderer orderCreator;
    private final CustomerFinder customerFinder;
    public CartHandler(Orderer orderCreator,CustomerFinder customerFinder){

        this.orderCreator = orderCreator;
        this.customerFinder = customerFinder;
    }
    private double priceProduct(Product product) {
        double itemsPrice = product.getItems().stream()
                .mapToDouble(ForfaitOffer::getPrice)
                .sum();
        return itemsPrice / product.getReduction();
    }

    public double computeCartPrice(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(e -> priceProduct(e.getProduct())* e.getQuantity())
                .sum();
    }


    public Order validate(Long customerId) throws CustomerIdNotFoundException, EmptyCartException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        if(customer.getCart().isEmpty()){
            throw new EmptyCartException(customer.getName());
        }
        return orderCreator.createOrder(customer);
    }

    public void addItem(Long customerId,Product p,int quantity) throws CustomerIdNotFoundException {
        Customer current = customerFinder.retrieveCustomer(customerId);
        Cart cart = current.getCart();
        if(quantity<0) throw new IllegalArgumentException("Quantity should be positive");
        cart.addItem(p,quantity);
    }

    public void removeItem(Long customerId,Product p,int quantity) throws CustomerIdNotFoundException {
        Customer current = customerFinder.retrieveCustomer(customerId);
        Cart cart = current.getCart();
        if(quantity<0) throw new IllegalArgumentException("Quantity should be positive");
        cart.removeItem(p,quantity);
    }
}
