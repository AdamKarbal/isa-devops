package fr.univcotedazur.skimaster.gate.controllers;

import fr.univcotedazur.skimaster.gate.handler.AccessHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import fr.univcotedazur.skimaster.gate.dtos.BeepContext;
import fr.univcotedazur.skimaster.gate.interfaces.AccessDealer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping(path = "/gatesync", produces = APPLICATION_JSON_VALUE)
public class GateSyncController {

    private final AccessHandler accessHandler;
    public static final String BASE_URI = "/gates";
    private final AccessDealer accessDealer;

    public GateSyncController(AccessHandler accessHandler, AccessDealer accessDealer) {
        this.accessHandler = accessHandler;
        this.accessDealer = accessDealer;
    }

    @PostMapping("/push")
    public void pushToGates() {
        accessHandler.synchronizeExternalSystems();
    }

    @PostMapping("/collect")
    public void collectFromGates(@RequestBody List<Long> nfcCardIds) {
        accessHandler.handleIncomingData(nfcCardIds);
    }
}
