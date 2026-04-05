package fr.univcotedazur.skimaster.cucumber.stats;


import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CucumberContextConfiguration
@SpringBootTest
public class StatisticsCucumberConfig {

    @MockitoBean
    private TurnstileProxy turnstileProxy;
}
