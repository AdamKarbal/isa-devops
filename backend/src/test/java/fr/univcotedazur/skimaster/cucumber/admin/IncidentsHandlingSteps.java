package fr.univcotedazur.skimaster.cucumber.admin;

import fr.univcotedazur.skimaster.admin.components.AdminService;
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

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class IncidentsHandlingSteps {

    @Autowired
    private AdminService adminService;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private ManualGateControl manualGateControl;

    private Exception lastException;
    private String gateId;

    @Before
    public void settingUpContext(){
        incidentRepository.deleteAll();
    }

    @Given("An {string} incident saying {string} exists for {string} gate and is closing")
    public void anIncidentExistsForGateAndIsClosing(String type, String description, String gateId) {
        this.gateId = gateId;
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

    @When("I try to open the gate while the incident is still unresolved")
    public void iTryToOpenTheGateWhileTheIncidentsIsStillUnresolved() {
        try{
            adminService.setStatus(gateId, "OPEN");
        }
        catch (Exception e){
            lastException = e;
        }
    }

    @Then("The system should refuse with an error message {string}")
    public void theSystemShouldRefuseWithAnErrorMessage(String expectedErrorMessage) {
        assertNotNull(lastException, "Ca aurait du lever une exeption.");
        assertEquals(expectedErrorMessage, lastException.getMessage());
    }

    @And("I resolve the incident")
    public void iResolveTheIncident() {
        adminService.resolveIncidentsForGate(gateId);
    }

    @Then("The system opens back the resolved gate")
    public void theSystemOpensBackTheResolvedGate() {
        //On verifie qu'il n'y a plus d'incidents ni fermant ni autres
        assertFalse(incidentService.hasUnresolvedIncidents(gateId));
        assertFalse(incidentService.hasUnresolvedClosingIncidents(gateId));
    }

    @And("I try to re-open the gate manually")
    public void iTryToReOpenTheGateManually() {
        lastException = null;

        //Pour mock l'exception renvoyée normalement par le service externe pas simulable ici
        org.mockito.Mockito.doThrow(new RuntimeException("Portique déjà ouvert"))
                .when(manualGateControl).openGate(gateId);

        try{
            adminService.setStatus(gateId, "OPEN");
        }
        catch (Exception e){
            lastException = e;
        }
    }

    @Then("I will get an error message {string} from the system telling that it is already open")
    public void iWillGetAnErrorMessageFromTheSystemTellingThatItIsAlreadyOpen(String expectedErrorMessage) {
        assertNotNull(lastException, "Ca aurait du lever une exeption.");
        assertEquals(expectedErrorMessage, lastException.getMessage());
    }
}
