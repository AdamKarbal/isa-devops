package fr.univcotedazur.skimaster.gate.controllers;


import fr.univcotedazur.skimaster.gate.dtos.BeepContext;
import fr.univcotedazur.skimaster.gate.interfaces.AccessDealer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = GateSyncController.BASE_URI,produces = APPLICATION_JSON_VALUE)
public class GateSyncController {

    public static final String BASE_URI = "/gates";
    private final AccessDealer accessDealer;

    public GateSyncController(AccessDealer accessDealer) {
        this.accessDealer = accessDealer;
    }

}
