package skimaster.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import skimaster.components.DisplayService;
import skimaster.dtos.DisplayAlertDTO;
import skimaster.dtos.DisplayAlertRequestDTO;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = DisplayController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class DisplayController {

    public static final String BASE_URI = "/display";
    private final DisplayService displayService;

    public DisplayController(DisplayService displayService) {
        this.displayService = displayService;
    }

    @PostMapping("/alerts")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void receiveAlert(@RequestBody DisplayAlertRequestDTO request) {
        displayService.pushAlert(request);
    }

    @GetMapping("/alerts")
    public List<DisplayAlertDTO> getAlerts() {
        return displayService.getAlerts();
    }

    @GetMapping("/alerts/{gateCode}")
    public List<DisplayAlertDTO> getAlertsForGateCode(@PathVariable String gateCode) {
        return displayService.getAlertsForGateCode(gateCode);
    }

    @DeleteMapping("/alerts")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearAlerts() {
        displayService.clearAlerts();
    }

    @DeleteMapping("/alerts/{gateCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearAlertsForGateCode(@PathVariable String gateCode) {
        displayService.clearAlertsForGateCode(gateCode);
    }

    @GetMapping("/id")
    public String getDisplayId() {
        return displayService.getDisplayId();
    }

    @GetMapping("/list")
    public List<String> listDisplays() {
        return displayService.getDistinctDisplayCodes();
    }
}
