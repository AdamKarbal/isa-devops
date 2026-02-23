package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class CustomerCommandsTest {

    private CustomerCommands customerCommands;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        RestClient restClient = builder.baseUrl("http://localhost:8080").build();
        customerCommands = new CustomerCommands(restClient, new CliContext());
    }

    @Test
    void showDisplaysCustomerProfile() {
        server.expect(requestTo("http://localhost:8080/customers/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{" +
                        "\"id\":1," +
                        "\"name\":\"Clienta\"," +
                        "\"creditCard\":\"1234567890\"}", MediaType.APPLICATION_JSON));

        String result = customerCommands.show(1L);

        assertThat(result).contains("Client #1", "Nom: Clienta");
        assertThat(result).doesNotContain("CreditCard");
        server.verify();
    }

    @Test
    void showReturnsCustomerNotFound() {
        server.expect(requestTo("http://localhost:8080/customers/404"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withResourceNotFound());

        String result = customerCommands.show(404L);

        assertThat(result).isEqualTo("client introuvable");
        server.verify();
    }

    @Test
    void passesDisplaysProductsForCustomer() {
        server.expect(requestTo("http://localhost:8080/customers/1/forfaits"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[" +
                        "{\"id\":11,\"customerId\":1,\"passType\":\"DAY\",\"ageCategory\":\"ADULT\",\"validDate\":\"2026-01-01\"}," +
                        "{\"id\":12,\"customerId\":1,\"passType\":\"WEEK\",\"ageCategory\":\"CHILD\",\"validDate\":\"2026-01-08\"}" +
                        "]", MediaType.APPLICATION_JSON));

        String result = customerCommands.passes(1L);

        assertThat(result).contains("Forfaits du client #1", "F11 type=DAY age=ADULT", "F12 type=WEEK age=CHILD");
        server.verify();
    }

    @Test
    void ordersDisplaysOrderHistory() {
        server.expect(requestTo("http://localhost:8080/customers/1/orders"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[" +
                        "{" +
                        "\"id\":91," +
                        "\"price\":120.0," +
                        "\"items\":[{\"id\":1001,\"reduction\":1},{\"id\":1002,\"reduction\":2}]" +
                        "}" +
                        "]", MediaType.APPLICATION_JSON));

        String result = customerCommands.orders(1L);

        assertThat(result).contains(
                "Historique des commandes du client #1",
                "commande #91",
                "montant=120",
                "produit #1001",
                "produit #1002"
        );
        server.verify();
    }

    @Test
    void ordersDisplaysEmptyMessageWhenNoHistory() {
        server.expect(requestTo("http://localhost:8080/customers/1/orders"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        String result = customerCommands.orders(1L);

        assertThat(result).isEqualTo("Aucune commande pour ce client");
        server.verify();
    }

    @Test
    void passesDisplaysEmptyMessageWhenNoPass() {
        server.expect(requestTo("http://localhost:8080/customers/1/forfaits"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        String result = customerCommands.passes(1L);

        assertThat(result).isEqualTo("Aucun forfait pour ce client");
        server.verify();
    }
}
