package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.CliContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(MockitoExtension.class)
class AdminCommandsTest {

    private MockRestServiceServer mockServer;
    private AdminCommands adminCommands;

    @Mock
    private CliContext cliContext;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        RestClient restClient = builder.build();

        adminCommands = new AdminCommands(restClient, cliContext);
    }

    @Test
    void shouldSendPostRequestToOpenGate() {
        String gateCode = "gate-1";

        mockServer.expect(requestTo("/admin/" + gateCode + "/open"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withNoContent());


        adminCommands.openGate(gateCode);


        mockServer.verify();
    }

    @Test
    void shouldFetchAndDisplayAlerts() {
        String jsonResponse = "[\"[FERMANT] Incident 1\", \"[NON FERMANT] Incident 2\"]";

        mockServer.expect(requestTo("/admin/alerts"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        adminCommands.checkGates();

        mockServer.verify();
    }
}
