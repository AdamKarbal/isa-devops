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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
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

    @Test
    void sendDisplayMessageCallsBackendEndpoint() {
        mockServer.expect(requestTo("http://localhost:8080/admin/display/message"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json("{\"message\":\"Attention brouillard\",\"displayCode\":null}"))
                .andRespond(withNoContent());

        String result = adminCommands.sendDisplayMessage("Attention brouillard");

        assertThat(result).isEqualTo("Message envoye au display.");
        mockServer.verify();
    }

    @Test
    void sendDisplayMessageTargetCallsBackendEndpoint() {
        mockServer.expect(requestTo("http://localhost:8080/admin/display/message"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json("{\"message\":\"Attention locale\",\"displayCode\":\"gate-2\"}"))
                .andRespond(withNoContent());

        String result = adminCommands.sendDisplayMessageTarget("gate-2", "Attention locale");

        assertThat(result).isEqualTo("Message envoye au display gate-2.");
        mockServer.verify();
    }

    @Test
    void clearDisplayCallsBackendEndpoint() {
        mockServer.expect(requestTo("http://localhost:8080/admin/display/message"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withNoContent());

        String result = adminCommands.clearDisplay();

        assertThat(result).isEqualTo("Display efface.");
        mockServer.verify();
    }

    @Test
    void setAlertThresholdCallsBackendEndpoint() {
        mockServer.expect(requestTo("http://localhost:8080/gates/gate-1/alert-threshold"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json("{\"alertThreshold\":42}"))
                .andRespond(withSuccess("{\"gateId\":\"gate-1\",\"status\":\"OPEN\",\"alertThreshold\":42}", MediaType.APPLICATION_JSON));

        String result = adminCommands.setAlertThreshold("gate-1", 42);

        assertThat(result).contains("gate-1", "alert-threshold=42", "status=OPEN");
        mockServer.verify();
    }

    @Test
    void listAlertThresholdsReadsBackendStatuses() {
        mockServer.expect(requestTo("http://localhost:8080/gates/status"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[" +
                        "{\"gateId\":\"gate-1\",\"status\":\"OPEN\",\"alertThreshold\":50}," +
                        "{\"gateId\":\"gate-2\",\"status\":\"CLOSED\",\"alertThreshold\":20}" +
                        "]", MediaType.APPLICATION_JSON));

        String result = adminCommands.listAlertThresholds();

        assertThat(result).contains("gate-1 status=OPEN alert-threshold=50");
        assertThat(result).contains("gate-2 status=CLOSED alert-threshold=20");
        mockServer.verify();
    }

    @Test
    void displayShowReadsTargetedDisplayAlerts() {
        mockServer.expect(requestTo("http://localhost:8080/admin/display/alerts/gate-1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"message\":\"Alerte\",\"gateCode\":\"gate-1\",\"receivedAt\":\"2026-03-21T18:00:00Z\"}]", MediaType.APPLICATION_JSON));

        String result = adminCommands.displayShow("gate-1");

        assertThat(result).contains("DISPLAY gate-1", "gate-1 -> Alerte");
        mockServer.verify();
    }

    @Test
    void monitorAllCallsStatsEndpoint() {
        mockServer.expect(requestTo("http://localhost:8080/stats/reports/daily"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"date\":\"2026-03-21\",\"totalPassages\":12,\"uniqueSkiers\":10,\"activeForfaits\":8}", MediaType.APPLICATION_JSON));

        String result = adminCommands.monitorAll();

        assertThat(result).contains("totalPassages=12", "uniqueSkiers=10", "activeForfaits=8");
        mockServer.verify();
    }

    @Test
    void monitorGateCallsOpeningTimeEndpoint() {
        mockServer.expect(requestTo("http://localhost:8080/stats/opening-time/gate-1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("120 secondes", MediaType.TEXT_PLAIN));

        String result = adminCommands.monitor("gate-1");

        assertThat(result).contains("Monitoring gate-1: 120 secondes");
        mockServer.verify();
    }
}
