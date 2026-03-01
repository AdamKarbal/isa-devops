package skimaster.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import skimaster.components.GateService;
import skimaster.dtos.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = GateController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class GateController {
    private final GateService gateService;
    public final static String BASE_URI = "/gate";
    public GateController(GateService gateService){
        this.gateService = gateService;
    }
    @PostMapping("/pass")
    public PassageResponseDTO pass(@RequestBody PassageRequestDTO request) {
        return gateService.processPassage(request);
    }

    @PostMapping("/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeGate() {
        gateService.closeGate();
    }

    @PostMapping("/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void openGate() {
        gateService.openGate();
    }

    @GetMapping("/status")
    public GateStatusResponseDTO getStatus() {
        return gateService.getStatus();
    }

    @GetMapping("/passages/{cardId}")
    public List<PassageDTO> getPassages(@PathVariable("cardId") Long cardId) {
        return gateService.getPassagesById(cardId);
    }

    @PostMapping("/incident")
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentDTO reportIncident(@RequestBody IncidentRequestDTO request) {
        return gateService.addIncident(request);
    }
    @GetMapping("/passages")
    public List<Long> getAllPassageIds() {
        return gateService.getAllPassages();
    }
}
