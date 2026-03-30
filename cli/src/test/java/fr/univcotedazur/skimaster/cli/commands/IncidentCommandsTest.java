package fr.univcotedazur.skimaster.cli.commands;

import fr.univcotedazur.skimaster.cli.model.IncidentTargetType;
import fr.univcotedazur.skimaster.cli.model.IncidentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;

class IncidentCommandsTest {

    private IncidentCommands incidentCommands;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        RestClient restClient = builder.baseUrl("http://localhost:8080").build();
        incidentCommands = new IncidentCommands(restClient);
    }

    @Test
    void reportSendsClosingFlagToBackend() {
        server.expect(requestTo("http://localhost:8080/incidents/addIncident"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json("""
                        {
                          "type":"EQUIPMENT_FAILURE",
                          "description":"Portique bloque",
                          "targetType":"PORTIQUE",
                          "targetCode":"gate-1",
                          "closes":true
                        }
                        """, false))
                .andRespond(withNoContent());

        String result = incidentCommands.report(
                IncidentType.EQUIPMENT_FAILURE,
                IncidentTargetType.PORTIQUE,
                "gate-1",
                "Portique bloque",
                true
        );

        assertThat(result).isEqualTo("Incident envoye au serveur avec succes.");
        server.verify();
    }
}
