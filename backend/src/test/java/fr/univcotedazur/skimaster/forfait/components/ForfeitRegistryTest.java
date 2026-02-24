package fr.univcotedazur.skimaster.forfait.components;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfeit;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.interfaces.FFinder;
import fr.univcotedazur.skimaster.forfait.interfaces.FRegister;
import fr.univcotedazur.skimaster.forfait.repositories.ForfeitOfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ForfeitRegistryTest {

    @Autowired
    private FRegister register;

    @Autowired
    private FFinder finder;

    @Autowired
    private ForfeitOfferRepository forfeitOfferRepository;

    private final Long userId = 1L;
    private ForfeitOffer savedOffer;
    private Forfeit newForfeit;

    @BeforeEach
    void setUp() {
        // Préparation de l'offre en base de données
        ForfeitOffer offer = new ForfeitOffer(AgeCategory.ADULT, PassType.WEEKLY, 70.0);
        savedOffer = forfeitOfferRepository.save(offer);

        // Liaison du forfait avec l'offre gérée par Hibernate
        newForfeit = new Forfeit(userId, LocalDate.of(2026, 2, 24), savedOffer);
    }

    @Test
    void noForfait() {
        List<Forfeit> forfeits = finder.findForfaitByUserId(userId);
        assertTrue(forfeits.isEmpty());
    }

    @Test
    void registerForfait() throws Exception {
        register.registerForfait(newForfeit, userId);

        List<Forfeit> fetchedForfeit = finder.findForfaitByUserId(userId);
        assertFalse(fetchedForfeit.isEmpty());
        // Comparaison basée sur l'égalité des objets. Si cela échoue, vérifie ta méthode equals() dans Forfait.
        assertEquals(newForfeit, fetchedForfeit.getFirst());
    }

    @Test
    void registerForfaits() throws Exception {
        register.registerForfait(newForfeit, userId);

        List<Forfeit> fetchedForfeit = finder.findForfaitByUserId(userId);
        assertFalse(fetchedForfeit.isEmpty());
        assertEquals(newForfeit, fetchedForfeit.getFirst());
    }
}
