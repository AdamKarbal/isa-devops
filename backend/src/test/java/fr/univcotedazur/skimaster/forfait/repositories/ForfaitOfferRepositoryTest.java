package fr.univcotedazur.skimaster.forfait.repositories;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.ForfaitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ForfaitOfferRepositoryTest {

    @Autowired
    private ForfaitOfferRepository forfaitOfferRepository;

    @Test
    void testIdGenerationAndFindByAgeCategoryAndPassType() {
        ForfaitOffer adultWeekOffer = new ForfaitOffer(AgeCategory.ADULT, PassType.WEEKLY, 300.0);
        Assertions.assertNull(adultWeekOffer.getId());

        forfaitOfferRepository.saveAndFlush(adultWeekOffer);

        Assertions.assertNotNull(adultWeekOffer.getId());
        Assertions.assertEquals(
                adultWeekOffer,
                forfaitOfferRepository.findByAgeCategoryAndPassType(AgeCategory.ADULT, PassType.WEEKLY).orElseThrow()
        );
    }

    @Test
    void testNegativePriceNotAllowed() {
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> forfaitOfferRepository.saveAndFlush(new ForfaitOffer(AgeCategory.CHILD, PassType.DAILY, -1.0))
        );
    }

}
