package fr.univcotedazur.skimaster.gate.controllers;

import fr.univcotedazur.skimaster.gate.handler.AccessHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/gatesync", produces = APPLICATION_JSON_VALUE)
public class GateSyncController {

    private final AccessHandler accessHandler;

    public GateSyncController(AccessHandler accessHandler) {
        this.accessHandler = accessHandler;
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
