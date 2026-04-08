package fr.univcotedazur.skimaster.cart.components;

import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cart.exceptions.ForfaitOfferIdNotFound;
import fr.univcotedazur.skimaster.cart.exceptions.ProductIdNotFound;
import fr.univcotedazur.skimaster.cart.interfaces.ProductCatalog;
import fr.univcotedazur.skimaster.cart.repositories.ProductRepository;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.repositories.ForfeitOfferRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProductCatalogHandler implements ProductCatalog {

    private final ProductRepository productRepository;
    private final ForfeitOfferRepository forfeitOfferRepository;

    public ProductCatalogHandler(ProductRepository productRepository, ForfeitOfferRepository forfeitOfferRepository) {
        this.productRepository = productRepository;
        this.forfeitOfferRepository = forfeitOfferRepository;
    }

    @Override
    public ForfeitOffer createForfaitOffer(AgeCategory ageCategory, PassType passType, double price) {
        return forfeitOfferRepository.save(new ForfeitOffer(ageCategory, passType, price));
    }

    @Override
    public Optional<ForfeitOffer> findForfaitOffer(AgeCategory ageCategory, PassType passType) {
        return forfeitOfferRepository.findByAgeCategoryAndPassType(ageCategory, passType);
    }

    @Override
    public Product createProduct(List<Long> forfaitOfferIds, int reduction) throws ForfaitOfferIdNotFound {
        List<ForfeitOffer> offers = new ArrayList<>();
        for (Long offerId : forfaitOfferIds) {
            ForfeitOffer offer = forfeitOfferRepository.findById(offerId)
                    .orElseThrow(() -> new ForfaitOfferIdNotFound("Forfait of id : "+offerId+" not found"));
            offers.add(offer);
        }
        return productRepository.save(new Product(offers, reduction));
    }

    @Override
    public Product retrieveProduct(Long productId) throws ProductIdNotFound {
        return productRepository.findById(productId).orElseThrow(() -> new ProductIdNotFound(productId.toString()));
    }
}
