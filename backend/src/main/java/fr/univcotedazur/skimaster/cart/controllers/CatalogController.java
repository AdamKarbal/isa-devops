package fr.univcotedazur.skimaster.cart.controllers;

import fr.univcotedazur.skimaster.cart.dtos.ProductCreationRequestDTO;
import fr.univcotedazur.skimaster.cart.dtos.ProductDTO;
import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cart.exceptions.ForfaitOfferIdNotFound;
import fr.univcotedazur.skimaster.cart.exceptions.ProductIdNotFound;
import fr.univcotedazur.skimaster.cart.interfaces.ProductCatalog;
import fr.univcotedazur.skimaster.forfait.dto.ForfaitOfferDTO;
import fr.univcotedazur.skimaster.forfait.dto.ForfaitOfferRequestDTO;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(path = CatalogController.BASE_URI,produces = APPLICATION_JSON_VALUE)
public class CatalogController {
    public static final String BASE_URI = "/catalog";
    private final ProductCatalog productCatalog;
    public CatalogController(ProductCatalog catalog){
        this.productCatalog=catalog;
    }
    @PostMapping
    public void doNothing(){

    }
    @PostMapping(path = "/offers", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ForfaitOfferDTO> createForfaitOffer(@RequestBody @Valid ForfaitOfferRequestDTO request) {
        ForfeitOffer offer = productCatalog.createForfaitOffer(request.ageCategory(), request.passType(), request.price());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(offer));
    }

    @GetMapping(path = "/offers")
    public ResponseEntity<ForfaitOfferDTO> findForfaitOffer(@RequestParam AgeCategory ageCategory, @RequestParam PassType passType) {
        Optional<ForfeitOffer> found = productCatalog.findForfaitOffer(ageCategory, passType);
        return found.map(offer -> ResponseEntity.ok(toDTO(offer))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/products", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid ProductCreationRequestDTO request) throws ForfaitOfferIdNotFound {
        Product created = productCatalog.createProduct(request.forfaitOfferIds(), request.reduction());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(created));
    }

    @GetMapping(path = "/products/{productId}")
    public ResponseEntity<ProductDTO> retrieveProduct(@PathVariable Long productId) throws ProductIdNotFound {
        return ResponseEntity.ok(toDTO(productCatalog.retrieveProduct(productId)));
    }

    private static ProductDTO toDTO(Product product) {
        return new ProductDTO(product.getId(), product.getItems().stream().map(ForfeitOffer::getId).toList(), product.getReduction());
    }

    private static ForfaitOfferDTO toDTO(ForfeitOffer offer) {
        return new ForfaitOfferDTO(offer.getId(), offer.getAgeCategory(), offer.getPassType(), offer.getPrice());
    }
}
