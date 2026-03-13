package fr.univcotedazur.skimaster.forfait.controllers;

import fr.univcotedazur.skimaster.common.dto.ErrorDTO;
import fr.univcotedazur.skimaster.forfait.components.ForfaitRegistry;
import fr.univcotedazur.skimaster.forfait.dto.ForfaitDTO;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.interfaces.FFinder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = ForfaitController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class ForfaitController {
    public static final String BASE_URI = "/forfaits";

    private final FFinder finder;

    private final ForfaitRegistry register;

    public ForfaitController(@Qualifier("forfaitFinder") FFinder finder, ForfaitRegistry register){
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
        List<Forfait> forfaits = finder.findAllForfaits();
        List<ForfaitDTO> dtos = forfaits.stream()
                .map(ForfaitController::forfaitToDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/catalogue")
    public ResponseEntity<List<PassType>>getCatalogue(){
        return ResponseEntity.ok(finder.getAvailablePassTypes());
    }

    private static ForfaitDTO forfaitToDTO(Forfait f){
        return new ForfaitDTO(
                f.getId(),
                f.getCustomerId(),
                f.getPassType().name(),
                f.getAgeCategory().name(),
                f.getValidDate(),
                f.getBeep()
                );
    }

}
