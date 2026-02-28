package fr.univcotedazur.skimaster.cart.components;

import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cart.exceptions.ForfaitOfferIdNotFound;
import fr.univcotedazur.skimaster.cart.exceptions.ProductIdNotFound;
import fr.univcotedazur.skimaster.cart.interfaces.ProductCatalog;
import fr.univcotedazur.skimaster.cart.repositories.ProductRepository;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.ForfaitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitOfferRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProductCatalogHandler implements ProductCatalog {

    private final ProductRepository productRepository;
    private final ForfaitOfferRepository forfaitOfferRepository;

    public ProductCatalogHandler(ProductRepository productRepository, ForfaitOfferRepository forfaitOfferRepository) {
        this.productRepository = productRepository;
        this.forfaitOfferRepository = forfaitOfferRepository;
    }

    @Override
    public ForfaitOffer createForfaitOffer(AgeCategory ageCategory, PassType passType, double price) {
        return forfaitOfferRepository.save(new ForfaitOffer(ageCategory, passType, price));
    }

    @Override
    public Optional<ForfaitOffer> findForfaitOffer(AgeCategory ageCategory, PassType passType) {
        return forfaitOfferRepository.findByAgeCategoryAndPassType(ageCategory, passType);
    }

    @Override
    public Product createProduct(List<Long> forfaitOfferIds, int reduction) throws ForfaitOfferIdNotFound {
        List<ForfaitOffer> offers = new ArrayList<>();
        for (Long offerId : forfaitOfferIds) {
            ForfaitOffer offer = forfaitOfferRepository.findById(offerId)
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
