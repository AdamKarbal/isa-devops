package fr.univcotedazur.skimaster.forfait.repositories;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ForfeitOfferRepositoryTest {

    @Autowired
    private ForfeitOfferRepository forfeitOfferRepository;

    @Test
    void testIdGenerationAndFindByAgeCategoryAndPassType() {
        ForfeitOffer adultWeekOffer = new ForfeitOffer(AgeCategory.ADULT, PassType.WEEKLY, 300.0);
        Assertions.assertNull(adultWeekOffer.getId());

        forfeitOfferRepository.saveAndFlush(adultWeekOffer);

        Assertions.assertNotNull(adultWeekOffer.getId());
        Assertions.assertEquals(
                adultWeekOffer,
                forfeitOfferRepository.findByAgeCategoryAndPassType(AgeCategory.ADULT, PassType.WEEKLY).orElseThrow()
        );
    }

    @Test
    void testNegativePriceNotAllowed() {
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> forfeitOfferRepository.saveAndFlush(new ForfeitOffer(AgeCategory.CHILD, PassType.FULL, -1.0))
        );
    }

}
