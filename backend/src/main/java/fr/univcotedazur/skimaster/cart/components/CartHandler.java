package fr.univcotedazur.skimaster.cart.components;

import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.cart.exceptions.EmptyCartException;
import fr.univcotedazur.skimaster.cart.exceptions.ProductIdNotFound;
import fr.univcotedazur.skimaster.cart.repositories.ProductRepository;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerFinder;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
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
    private final ProductRepository productRepository;
    public CartHandler(Orderer orderCreator, CustomerFinder customerFinder, ProductRepository productRepository){
        this.orderCreator = orderCreator;
        this.customerFinder = customerFinder;
        this.productRepository = productRepository;
    }
    private double priceProduct(Product product) {
        double itemsPrice = product.getItems().stream()
                .mapToDouble(ForfeitOffer::getPrice)
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

    public void addItem(Long customerId,Long productId,int quantity) throws CustomerIdNotFoundException {
        if(quantity<0) throw new IllegalArgumentException("Quantity should be positive");
        Customer current = customerFinder.retrieveCustomer(customerId);
        Cart cart = current.getCart();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductIdNotFound("Product of id "+productId+" not found"));
        cart.addItem(product,quantity);
    }

    public void removeItem(Long customerId,Long productId,int quantity) throws CustomerIdNotFoundException {
        if(quantity<0) throw new IllegalArgumentException("Quantity should be positive");
        Customer current = customerFinder.retrieveCustomer(customerId);
        Cart cart = current.getCart();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductIdNotFound("Product of id "+productId+" not found"));
        cart.removeItem(product,quantity);
    }
}
