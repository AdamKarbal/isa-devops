package fr.univcotedazur.skimaster.cucumber;

import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cart.exceptions.EmptyCartException;
import fr.univcotedazur.skimaster.cart.interfaces.CartModifier;
import fr.univcotedazur.skimaster.cart.interfaces.CartProcessor;
import fr.univcotedazur.skimaster.cart.interfaces.ProductCatalog;
import fr.univcotedazur.skimaster.cart.repositories.ProductRepository;
import fr.univcotedazur.skimaster.customer.components.CustomerRegistry;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.exceptions.AlreadyExistingForfaitException;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitRepository;
import fr.univcotedazur.skimaster.forfait.repositories.ForfeitOfferRepository;
import fr.univcotedazur.skimaster.gate.components.AccessHandler;
import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateStatusResponseDTO;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;
import fr.univcotedazur.skimaster.gate.exceptions.GateClosedException;
import fr.univcotedazur.skimaster.gate.exceptions.NoGateExistentException;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.persistence.EntityManager;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ErrorTestSteps {

    @Autowired
    private List<CrudRepository<?, ?>> repositories;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CustomerRegistry customerRegistry;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ForfeitOfferRepository forfaitOfferRepository;

    @Autowired
    private ForfaitRepository forfaitRepository;

    @Autowired
    private CartProcessor cartProcessor;

    @Autowired
    private CartModifier cartModifier;

    @Autowired
    private ProductCatalog productCatalog;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AccessHandler accessHandler;

    @Mock
    TurnstileProxy turnstileProxy = mock(TurnstileProxy.class);

    private Customer customer;
    private ForfeitOffer forfeitOffer;
    private Forfait forfait;
    private BeepContext beepContext;
    private BeepContext beepWrongGate;
    private long unregisteredCustomerId;
    private Product product;

    @Before
    public void setUp() {
        productRepository.deleteAll();
        forfaitRepository.deleteAll();       // child
        forfaitOfferRepository.deleteAll();  // parent
        customerRepository.deleteAll();
    }

    @Given("a client named {string} with the credit card {string}")
    public void setUpClient(String client, String creditCard) throws AlreadyExistingCustomerException {
        customer = customerRegistry.register(client,creditCard);
    }

    @And("an adult dayly forfait with a price of {int}")
    public void setUpAdultDaylyForfait(int price) {
        forfeitOffer = new ForfeitOffer(AgeCategory.ADULT, PassType.DAILY, price);
        forfaitOfferRepository.save(forfeitOffer);
        product = productCatalog.createProduct(List.of(forfeitOffer.getId(),forfeitOffer.getId()),5);
    }

    @And("the client gets the forfait")
    public void clientBuysTheForfait() {
        forfait = new Forfait(customer.getId(), LocalDate.now(),forfeitOffer);
        forfaitRepository.save(forfait);
    }

    @And("a closed gate")
    public void closedGate() {
        when(turnstileProxy.getGateStatus(anyString())).thenReturn(new GateStatusResponseDTO("gate-1", "CLOSED", 0));
    }


    @When("a client tries to register a second time")
    public void clientTriesToRegisterASecondTime() {}

    @Then("a error idicating they are already registered is received")
    public void errorIdicatingTheyAreAlreadyRegistered() {
        assertThrows(AlreadyExistingCustomerException.class, () -> {customerRegistry.register(customer.getName(),customer.getCreditCard());});
    }


    @When("looking for an unregistered client with id {int}")
    public void lookingForAnUnregisteredClient(int id) {
        unregisteredCustomerId = id;
        System.out.println(customerRegistry.findById(unregisteredCustomerId));
    }

    @Then("an error indicating the client can't be found is received")
    public void errorIndicatingTheClientCanBeFoundIsReceived() {
        assertTrue(customerRegistry.findById(unregisteredCustomerId).isEmpty());
    }

    @When("a client tries to validate an empty cart")
    public void clientTriesToValidateAnEmptyCart() {}

    @Then("an error indicating the cart is empty is received")
    public void errorIndicatingTheCartIsEmptyIsReceived() {
        assertThrows(EmptyCartException.class, () -> cartProcessor.validate(customer.getId()));
    }

    @When("a client put 1 product in his empty cart")
    public void clientPut1ProductInHisEmptyCart() {
        customer.getCart().addItem(product,1);
    }

    @And("tries to remove 2 products from his cart")
    public void triesToRemove2ProductsFromHisCart() {}

    @Then("an error indicating the quantity of items to remove is wrong is received")
    public void errorIndicatingTheQuantityOfItemsIsWrongIsReceived() {
        assertThrows(IllegalArgumentException.class, () -> cartModifier.removeItem(customer.getId(),product.getId(),2));
    }
}
