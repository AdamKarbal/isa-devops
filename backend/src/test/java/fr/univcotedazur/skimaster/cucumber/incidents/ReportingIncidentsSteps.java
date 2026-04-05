package fr.univcotedazur.skimaster.cucumber.incidents;

import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import fr.univcotedazur.skimaster.incident.components.IncidentService;
import fr.univcotedazur.skimaster.incident.dto.IncidentDTO;
import fr.univcotedazur.skimaster.incident.entities.IncidentTargetType;
import fr.univcotedazur.skimaster.incident.entities.IncidentType;
import fr.univcotedazur.skimaster.incident.repositories.IncidentRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@Transactional
public class ReportingIncidentsSteps {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private ManualGateControl manualGateControl;

    @Before
    public void settingUpContext() {
        incidentRepository.deleteAll();
    }

    @Given("No incidents exist in the database")
    public void noIncidentsExistInTheDatabase() {
        assertEquals(0, incidentRepository.count());
    }

    @When("I report a blocking {string} incident on gate {string} with description {string}")
    public void iReportABlockingIncidentOnGateWithDescription(String type, String gateId, String description) {
        IncidentDTO dto = new IncidentDTO(
                1L,
                IncidentType.valueOf(type),
                description,
                java.time.LocalDateTime.now(),
                IncidentTargetType.PORTIQUE,
                gateId,
                true
        );

        incidentService.reportIncident(dto);
    }

    @Then("the database should contain {int} incident")
    public void theDatabaseShouldContainIncident(int expectedCount) {
        assertEquals(expectedCount, incidentRepository.count());
    }

    @And("the gate {string} should receive a close command")
    public void theGateShouldReceiveACloseCommand(String gateId) {
        verify(manualGateControl).closeGate(gateId);
    }
}
