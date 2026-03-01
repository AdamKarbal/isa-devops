package fr.univcotedazur.skimaster.forfait.components;

import fr.univcotedazur.skimaster.forfait.dto.ForfaitDTO;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.ForfaitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
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

@SpringBootTest(properties = {"gate.host.baseurl=http://localhost:8082"})

@Transactional
public class ForfaitRegistryTest {

    @Autowired
    private FRegister register;

    @Autowired
    private ForfaitRegistry finder;

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
        List<ForfaitDTO> forfaits = finder.findForfaitsByCustomerId(userId);
        assertTrue(forfaits.isEmpty());
    }

    @Test
    void registerForfait() throws Exception {
        register.registerForfait(newForfait, userId);

        List<ForfaitDTO> fetchedForfait = finder.findForfaitsByCustomerId(userId);
        assertFalse(fetchedForfait.isEmpty());
        assertEquals(userId, fetchedForfait.getFirst().customerId());
        assertEquals(PassType.WEEKLY.name(), fetchedForfait.getFirst().passType());
        assertEquals(AgeCategory.ADULT.name(), fetchedForfait.getFirst().ageCategory());
        assertEquals(LocalDate.of(2026, 2, 24), fetchedForfait.getFirst().validDate());
    }

    @Test
    void registerForfaits() throws Exception {
        Forfait secondForfait = new Forfait(userId, LocalDate.of(2026, 2, 25), savedOffer);
        register.registerForfaits(List.of(newForfait, secondForfait), userId);

        List<ForfaitDTO> fetchedForfait = finder.findForfaitsByCustomerId(userId);
        assertEquals(2, fetchedForfait.size());
    }

    @Test
    void getAvailablePassTypes_returnsAllValues() {
        List<PassType> passTypes = finder.getAvailablePassTypes();
        assertEquals(List.of(PassType.values()), passTypes);
    }
}
