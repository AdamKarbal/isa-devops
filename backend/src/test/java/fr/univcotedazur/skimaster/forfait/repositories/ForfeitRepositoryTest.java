package fr.univcotedazur.skimaster.forfait.repositories;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfeit;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
public class ForfeitRepositoryTest {

    @Autowired
    private ForfeitRepository forfeitRepository;

    @Autowired
    private ForfeitOfferRepository forfeitOfferRepository;

    private final Long userId = 1L;
    private ForfeitOffer savedOffer;
    private Forfeit forfeit;

    @BeforeEach
    void setUp() {
        // Sauvegarde de l'offre d'abord pour éviter l'erreur TransientPropertyValueException
        ForfeitOffer offer = new ForfeitOffer(AgeCategory.ADULT, PassType.WEEKLY, 70.0);
        savedOffer = forfeitOfferRepository.save(offer);

        // Création du forfait avec l'offre persistée
        forfeit = new Forfeit(userId, LocalDate.of(2026, 2, 24), savedOffer);
    }

    @Test
    void testIdGenerationAndUnicity() {
        Assertions.assertNull(forfeit.getId());
        forfeitRepository.saveAndFlush(forfeit);
        Assertions.assertNotNull(forfeit.getId());
    }

    @Test
    void testFindForfaitByUserId() {
        forfeitRepository.saveAndFlush(forfeit);
        Assertions.assertEquals(List.of(forfeit), forfeitRepository.findByUserId(userId));
    }

    @Test
    void testNullUserId() {
        Forfeit invalidForfeit = new Forfeit(null, LocalDate.of(2026, 2, 24), savedOffer);
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                forfeitRepository.saveAndFlush(invalidForfeit)
        );
    }

    @Test
    void testNullVDate() {
        Forfeit invalidForfeit = new Forfeit(userId, null, savedOffer);
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                forfeitRepository.saveAndFlush(invalidForfeit)
        );
    }

    @Test
    void testNullAgeCategory() {
        ForfeitOffer invalidOffer = new ForfeitOffer(null, PassType.WEEKLY, 70.0);
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                forfeitOfferRepository.saveAndFlush(invalidOffer)
        );
    }

    @Test
    void testNullPassType() {
        ForfeitOffer invalidOffer = new ForfeitOffer(AgeCategory.ADULT, null, 70.0);
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                forfeitOfferRepository.saveAndFlush(invalidOffer)
        );
    }
}
