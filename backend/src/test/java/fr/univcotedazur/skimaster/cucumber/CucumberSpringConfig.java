package fr.univcotedazur.skimaster.cucumber;

import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import fr.univcotedazur.skimaster.incident.interfaces.DisplayNotifier;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CucumberContextConfiguration
@SpringBootTest
public class CucumberSpringConfig {
    @MockitoBean private TurnstileProxy turnstileProxy;
    @MockitoBean private ManualGateControl manualGateControl;
    @MockitoBean private DisplayNotifier displayNotifier;
}
