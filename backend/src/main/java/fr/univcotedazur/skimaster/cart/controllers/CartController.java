package fr.univcotedazur.skimaster.cart.controllers;

import fr.univcotedazur.skimaster.cart.dtos.CartItemAddedDTO;
import fr.univcotedazur.skimaster.cart.dtos.CartItemRequestDTO;
import fr.univcotedazur.skimaster.cart.exceptions.EmptyCartException;
import fr.univcotedazur.skimaster.cart.exceptions.ProductIdNotFound;
import fr.univcotedazur.skimaster.cart.interfaces.CartModifier;
import fr.univcotedazur.skimaster.cart.interfaces.CartProcessor;
import fr.univcotedazur.skimaster.cart.repositories.ProductRepository;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerFinder;
import fr.univcotedazur.skimaster.order.dtos.OrderCreatedDTO;
import fr.univcotedazur.skimaster.order.entities.Order;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CartController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class CartController {
    public static final String BASE_URI = "/cart";
    private final CustomerFinder customerFinder;
    private final CartModifier cartModifier;
    private final CartProcessor cartProcessor;
    private final ProductRepository productRepository;
    public CartController(CartModifier cartModifier, CartProcessor cartProcessor, CustomerFinder customerFinder, ProductRepository productRepository) {
        this.cartModifier = cartModifier;
        this.cartProcessor = cartProcessor;
        this.customerFinder = customerFinder;
        this.productRepository = productRepository;
    }
    @PostMapping(path = "/{customerId}/add",consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addItem(@PathVariable("customerId") Long customerId, @RequestBody @Valid CartItemRequestDTO infos) throws CustomerIdNotFoundException, ProductIdNotFound {
        cartModifier.addItem(customerId, infos.productId(), infos.quantity());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{customerId}/remove",consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeItem(@PathVariable("customerId") Long customerId, @RequestBody @Valid CartItemRequestDTO infos) throws CustomerIdNotFoundException, ProductIdNotFound {
        cartModifier.removeItem(customerId, infos.productId(), infos.quantity());
        return ResponseEntity.noContent().build();
    }
    @GetMapping(path = "/{customerId}/price",consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> price(@PathVariable("customerId") Long customerId) throws CustomerIdNotFoundException{
        double price = cartProcessor.computeCartPrice(customerFinder.retrieveCustomer(customerId).getCart());
        return ResponseEntity.ok(price);
    }
    @PostMapping(path = "/{customerId}/validate",consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderCreatedDTO> validate(@PathVariable("customerId") Long customerId) throws CustomerIdNotFoundException, EmptyCartException {
        Order validated = cartProcessor.validate(customerId);
        return ResponseEntity.ok(new OrderCreatedDTO(validated.getOrderId(),validated.getItems(),validated.getPrice()));
    }

}
