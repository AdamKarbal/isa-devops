package fr.univcotedazur.skimaster.customer.controllers;

import fr.univcotedazur.skimaster.common.dto.ErrorDTO;
import fr.univcotedazur.skimaster.customer.dto.CustomerDTO;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerFinder;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerRegistration;
import fr.univcotedazur.skimaster.forfait.components.ForfaitFinder;
import fr.univcotedazur.skimaster.forfait.dto.ForfaitDTO;
import fr.univcotedazur.skimaster.order.dto.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CustomerCareController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class CustomerCareController {

    public static final String BASE_URI = "/customers";

    private final CustomerRegistration registry;

    private final CustomerFinder finder;
    private final ForfaitFinder forfaitFinder;

    public CustomerCareController(CustomerRegistration registry, CustomerFinder finder, ForfaitFinder forfaitFinder) {
        this.registry = registry;
        this.finder = finder;
        this.forfaitFinder = forfaitFinder;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    // The 422 (Unprocessable Entity) status code means the server understands the content type of the request entity
    // (hence a 415(Unsupported Media Type) status code is inappropriate), and the syntax of the request entity is
    // correct (thus a 400 (Bad Request) status code is inappropriate) but was unable to process the contained
    // instructions.
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        return new ErrorDTO("Cannot process Customer information", e.getMessage());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> register(@RequestBody @Valid CustomerDTO cusdto) {
        // Note that there is no validation at all on the CustomerDto mapped
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertCustomerToDto(registry.register(cusdto.name(), cusdto.creditCard())));
        } catch (AlreadyExistingCustomerException e) {
            // Note: Returning 409 (Conflict) can also be seen a security/privacy vulnerability, exposing a service for account enumeration
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getCustomers() {
        return ResponseEntity.ok(finder.findAll().stream().map(CustomerCareController::convertCustomerToDto).toList());
    }

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("customerId") Long customerId) throws CustomerIdNotFoundException {
        return ResponseEntity.ok(convertCustomerToDto(finder.retrieveCustomer(customerId)));
    }

    private static CustomerDTO convertCustomerToDto(Customer customer) { // In more complex cases, we could use a ModelMapper such as MapStruct
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getCreditCard());
    }

    @ExceptionHandler(CustomerIdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleCustomerNotFound(CustomerIdNotFoundException e) {
        Long id = e.getId();
        return new ErrorDTO("client introuvable", "customer_id=" + id);
    }

    @GetMapping(path = "/{customerId}/forfaits")
    public ResponseEntity<List<ForfaitDTO>> getForfaits(@PathVariable("customerId") Long customerId)
            throws CustomerIdNotFoundException {

        finder.retrieveCustomer(customerId); // déclenche 404 si absent
        return ResponseEntity.ok(forfaitFinder.findForfaitsByCustomerId(customerId));
    }

    @GetMapping(path ="/{customerId}/forfaits/{forfaitId}")
    public ResponseEntity<ForfaitDTO> getOrderById(@PathVariable("customerId") Long customerId, @PathVariable("forfaitId") Long forfaitId) {
        return ResponseEntity.ok(forfaitFinder.getForfait(customerId, forfaitId));
    }
}

