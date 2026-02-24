package fr.univcotedazur.skimaster.forfait.repositories;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.ForfaitOffer;
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
public class ForfaitRepositoryTest {

    @Autowired
    private ForfaitRepository forfaitRepository;

    @Autowired
    private ForfaitOfferRepository forfaitOfferRepository;

    private final Long userId = 1L;
    private ForfaitOffer savedOffer;
    private Forfait forfait;

    @BeforeEach
    void setUp() {
        // Sauvegarde de l'offre d'abord pour éviter l'erreur TransientPropertyValueException
        ForfaitOffer offer = new ForfaitOffer(AgeCategory.ADULT, PassType.WEEKLY, 70.0);
        savedOffer = forfaitOfferRepository.save(offer);

        // Création du forfait avec l'offre persistée
        forfait = new Forfait(userId, LocalDate.of(2026, 2, 24), savedOffer);
    }

    @Test
    void testIdGenerationAndUnicity() {
        Assertions.assertNull(forfait.getId());
        forfaitRepository.saveAndFlush(forfait);
        Assertions.assertNotNull(forfait.getId());
    }

    @Test
    void testFindForfaitByUserId() {
        forfaitRepository.saveAndFlush(forfait);
        Assertions.assertEquals(List.of(forfait), forfaitRepository.findByUserId(userId));
    }

    @Test
    void testNullUserId() {
        Forfait invalidForfait = new Forfait(null, LocalDate.of(2026, 2, 24), savedOffer);
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                forfaitRepository.saveAndFlush(invalidForfait)
        );
    }

    @Test
    void testNullVDate() {
        Forfait invalidForfait = new Forfait(userId, null, savedOffer);
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                forfaitRepository.saveAndFlush(invalidForfait)
        );
    }

    @Test
    void testNullAgeCategory() {
        ForfaitOffer invalidOffer = new ForfaitOffer(null, PassType.WEEKLY, 70.0);
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                forfaitOfferRepository.saveAndFlush(invalidOffer)
        );
    }

    @Test
    void testNullPassType() {
        ForfaitOffer invalidOffer = new ForfaitOffer(AgeCategory.ADULT, null, 70.0);
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                forfaitOfferRepository.saveAndFlush(invalidOffer)
        );
    }
}
