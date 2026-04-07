package fr.univcotedazur.skimaster.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

@CucumberContextConfiguration
@SpringBootTest
@Transactional
@ComponentScan(basePackages = "fr.univcotedazur.skimaster.cucumber")
public class CucumberConfig {
}
