package fr.univcotedazur.skimaster.cart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univcotedazur.skimaster.cart.entities.Cart;
import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cart.exceptions.ProductIdNotFound;
import fr.univcotedazur.skimaster.cart.interfaces.CartModifier;
import fr.univcotedazur.skimaster.cart.interfaces.CartProcessor;
import fr.univcotedazur.skimaster.cart.interfaces.ProductCatalog;
import fr.univcotedazur.skimaster.common.controllers.GlobalControllerAdvice;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerFinder;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.cart.exceptions.ForfaitOfferIdNotFound;
import fr.univcotedazur.skimaster.order.entities.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CartController.class)
@Import(GlobalControllerAdvice.class)
class CartControllerTest {

    /*@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CartModifier cartModifier;

    @MockitoBean
    private CartProcessor cartProcessor;

    @MockitoBean
    private ProductCatalog productCatalog;

    @MockitoBean
    private CustomerFinder customerFinder;

    @Test
    void returnsNotFoundWhenCustomerDoesNotExist() throws Exception {
        when(customerFinder.retrieveCustomer(404L)).thenThrow(new CustomerIdNotFoundException(404L));

        mockMvc.perform(get("/carts/404/price"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Customer not found"));
    }

    @Test
    void addItemUsesProductIdAndQuantity() throws Exception {
        doNothing().when(cartModifier).addItem(5L, 12L, 3);

        mockMvc.perform(post("/carts/5/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Request(12L, 3))))
                .andExpect(status().isNoContent());

        verify(cartModifier).addItem(5L, 12L, 3);
    }

    @Test
    void removeItemUsesProductIdAndQuantity() throws Exception {
        doNothing().when(cartModifier).removeItem(5L, 12L, 2);

        mockMvc.perform(delete("/carts/5/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Request(12L, 2))))
                .andExpect(status().isNoContent());

        verify(cartModifier).removeItem(5L, 12L, 2);
    }

    /*@Test
    void createAndFindForfaitOffer() throws Exception {
        ForfaitOffer offer = new ForfaitOffer(AgeCategory.ADULT, PassType.DAILY, 45.0);
        when(productCatalog.createForfaitOffer(AgeCategory.ADULT, PassType.DAILY, 45.0)).thenReturn(offer);
        when(productCatalog.findForfaitOffer(AgeCategory.ADULT, PassType.DAILY)).thenReturn(Optional.of(offer));

        mockMvc.perform(post("/carts/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ForfaitOfferRequestDTO(AgeCategory.ADULT, PassType.DAILY, 45.0))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ageCategory").value("ADULT"));

        mockMvc.perform(get("/carts/offers?ageCategory=ADULT&passType=DAILY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passType").value("DAILY"));
    }

    @Test
    void createProductFromOfferIds() throws Exception {
        ForfaitOffer offer = new ForfaitOffer(AgeCategory.ADULT, PassType.DAILY, 45.0);
        Product product = new Product(List.of(offer), 10);
        when(productCatalog.createProduct(List.of(1L), 10)).thenReturn(product);

        mockMvc.perform(post("/carts/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductCreationRequestDTO(List.of(1L), 10))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reduction").value(10));
    }

    @Test
    void returnsNotFoundWhenForfaitOfferMissingForProductCreation() throws Exception {
        doThrow(new ForfaitOfferIdNotFound(4L)).when(productCatalog).createProduct(List.of(4L), 10);

        mockMvc.perform(post("/carts/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductCreationRequestDTO(List.of(4L), 10))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Forfait offer not found"));
    }

    @Test
    void computePriceFromCustomerCart() throws Exception {
        Customer customer = new Customer("john", "1234567890");
        customer.setCart(new Cart());
        when(customerFinder.retrieveCustomer(5L)).thenReturn(customer);
        when(cartProcessor.computeCartPrice(customer.getCart())).thenReturn(25.5);

        mockMvc.perform(get("/carts/5/price"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(25.5));
    }

    @Test
    void returnsNotFoundWhenCustomerDoesNotExist() throws Exception {
        when(customerFinder.retrieveCustomer(404L)).thenThrow(new CustomerIdNotFoundException(404L));

        mockMvc.perform(get("/carts/404/price"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Customer not found"));
    }

    @Test
    void validateCartCallsProcessor() throws Exception {
        when(cartProcessor.validate(7L)).thenReturn(new Order());

        mockMvc.perform(post("/carts/7/validate"))
                .andExpect(status().isOk());

        verify(cartProcessor).validate(7L);
    }*/

    private record Request(Long productId, Integer quantity) {
    }
}
