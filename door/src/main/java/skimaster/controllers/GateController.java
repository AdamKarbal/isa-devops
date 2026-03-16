package skimaster.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;
import skimaster.components.GateService;
import skimaster.dtos.*;
import skimaster.dtos.GateSyncDTO;

import java.util.List;
import java.util.Optional;

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
        Optional<PassageResponseDTO> responseDTO = gateService.processPassage(request);
        if (responseDTO.isEmpty()) {
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                    HttpStatus.FORBIDDEN,
                    "Le client avec la carte " + request.nfcCardId() + " ne peut pas passer"
            );
            throw new ErrorResponseException(HttpStatus.FORBIDDEN, problem, null);
        }
        return responseDTO.get();
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
    public List<PassageDTO> getAllPassageIds() {
        return gateService.getAllPassages();
    }
    @PostMapping("/sync")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void syncWhitelist(@RequestBody GateSyncDTO dto) {
        gateService.updateWhitelist(dto.authorizedCardIds());
    }

    @GetMapping("/passages/today")
    public List<PassageDTO> getPassagesOfToday() {
        return gateService.getTodayPassages();
    }
}
