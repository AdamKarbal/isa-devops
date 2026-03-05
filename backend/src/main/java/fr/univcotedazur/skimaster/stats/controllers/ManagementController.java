package fr.univcotedazur.skimaster.stats.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = ManagementController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class ManagementController {
    public static final String BASE_URI = "/stats/";

    public ManagementController() {

    }
}
