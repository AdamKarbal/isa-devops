package fr.univcotedazur.skimaster.forfait.controllers;

import fr.univcotedazur.skimaster.common.dto.ErrorDTO;
import fr.univcotedazur.skimaster.forfait.components.ForfeitRegistry;
import fr.univcotedazur.skimaster.forfait.dto.ForfaitDTO;
import fr.univcotedazur.skimaster.forfait.entities.Forfeit;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.interfaces.FFinder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = ForfeitController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class ForfeitController {
    public static final String BASE_URI = "/forfaits";

    private final FFinder finder;

    private final ForfeitRegistry register;

    public ForfeitController(FFinder finder, ForfeitRegistry register){
        this.register = register;
        this.finder=finder;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        return new ErrorDTO("Cannot process Customer information", e.getMessage());
    }

    @GetMapping
    public ResponseEntity<List<ForfaitDTO>>getAllForfaits(){
        List<Forfeit> forfeits = finder.findAllForfaits();
        List<ForfaitDTO> dtos = forfeits.stream()
                .map(ForfeitController::forfaitToDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/catalogue")
    public ResponseEntity<List<PassType>>getCatalogue(){
        return ResponseEntity.ok(finder.getAvailablePassTypes());
    }

    private static ForfaitDTO forfaitToDTO(Forfeit f){
        return new ForfaitDTO(
                f.getId(),
                f.getUserId(),
                f.getPassType().name(),
                f.getAgeCategory().name(),
                f.getValidDate()
                );
    }

}
