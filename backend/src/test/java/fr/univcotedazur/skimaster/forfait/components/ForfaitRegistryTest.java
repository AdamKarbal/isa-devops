package fr.univcotedazur.skimaster.forfait.components;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.ForfaitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.interfaces.FFinder;
import fr.univcotedazur.skimaster.forfait.interfaces.FRegister;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitOfferRepository;
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
public class ForfaitRegistryTest {

    @Autowired
    private FRegister register;

    @Autowired
    private FFinder finder;

    @Autowired
    private ForfaitOfferRepository forfaitOfferRepository;

    private final Long userId = 1L;
    private ForfaitOffer savedOffer;
    private Forfait newForfait;

    @BeforeEach
    void setUp() {
        // Préparation de l'offre en base de données
        ForfaitOffer offer = new ForfaitOffer(AgeCategory.ADULT, PassType.WEEKLY, 70.0);
        savedOffer = forfaitOfferRepository.save(offer);

        // Liaison du forfait avec l'offre gérée par Hibernate
        newForfait = new Forfait(userId, LocalDate.of(2026, 2, 24), savedOffer);
    }

    @Test
    void noForfait() {
        List<Forfait> forfaits = finder.findForfaitByUserId(userId);
        assertTrue(forfaits.isEmpty());
    }

    @Test
    void registerForfait() throws Exception {
        register.registerForfait(newForfait, userId);

        List<Forfait> fetchedForfait = finder.findForfaitByUserId(userId);
        assertFalse(fetchedForfait.isEmpty());
        // Comparaison basée sur l'égalité des objets. Si cela échoue, vérifie ta méthode equals() dans Forfait.
        assertEquals(newForfait, fetchedForfait.getFirst());
    }

    @Test
    void registerForfaits() throws Exception {
        register.registerForfait(newForfait, userId);

        List<Forfait> fetchedForfait = finder.findForfaitByUserId(userId);
        assertFalse(fetchedForfait.isEmpty());
        assertEquals(newForfait, fetchedForfait.getFirst());
    }
}
