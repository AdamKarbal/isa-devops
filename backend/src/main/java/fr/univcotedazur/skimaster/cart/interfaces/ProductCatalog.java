package fr.univcotedazur.skimaster.cart.interfaces;

import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.cart.exceptions.ForfaitOfferIdNotFound;
import fr.univcotedazur.skimaster.cart.exceptions.ProductIdNotFound;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;


import java.util.List;
import java.util.Optional;

public interface ProductCatalog {

    ForfeitOffer createForfaitOffer(AgeCategory ageCategory, PassType passType, double price);

    Optional<ForfeitOffer> findForfaitOffer(AgeCategory ageCategory, PassType passType);

    Product createProduct(List<Long> forfaitOfferIds, int reduction) throws ForfaitOfferIdNotFound;

    Product retrieveProduct(Long productId) throws ProductIdNotFound;
}
