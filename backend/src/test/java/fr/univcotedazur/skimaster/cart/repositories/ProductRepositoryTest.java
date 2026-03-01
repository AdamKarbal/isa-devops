package fr.univcotedazur.skimaster.cart.repositories;

import fr.univcotedazur.skimaster.cart.entities.Product;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.repositories.ForfeitOfferRepository;
import fr.univcotedazur.skimaster.forfait.repositories.ForfeitOfferRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ForfeitOfferRepository forfaitOfferRepository;

    @Test
    void testIdGenerationAndPersistence() {
        ForfeitOffer adultDailyOffer = forfaitOfferRepository.saveAndFlush(
                new ForfeitOffer(AgeCategory.ADULT, PassType.DAILY, 50.0)
        );
        Product product = new Product(List.of(adultDailyOffer), 10);

        Assertions.assertNull(product.getId());

        productRepository.saveAndFlush(product);

        Assertions.assertNotNull(product.getId());
        Product savedProduct = productRepository.findById(product.getId()).orElseThrow();
        Assertions.assertEquals(1, savedProduct.getItems().size());
        Assertions.assertEquals(10, savedProduct.getReduction());
    }

    @Test
    void testReductionMustBeAtLeastOne() {
        ForfeitOffer adultDailyOffer = forfaitOfferRepository.saveAndFlush(
                new ForfeitOffer(AgeCategory.ADULT, PassType.DAILY, 50.0)
        );

        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> productRepository.saveAndFlush(new Product(List.of(adultDailyOffer), 0))
        );
    }

}
