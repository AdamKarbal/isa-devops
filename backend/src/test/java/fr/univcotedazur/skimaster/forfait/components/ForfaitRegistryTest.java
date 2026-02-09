package fr.univcotedazur.skimaster.forfait.components;

import fr.univcotedazur.skimaster.customer.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.exceptions.AlreadyExistingForfaitException;
import fr.univcotedazur.skimaster.forfait.interfaces.FFinder;
import fr.univcotedazur.skimaster.forfait.interfaces.FRegister;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ForfaitRegistryTest {

    @Autowired
    private FRegister register;

    @Autowired
    private FFinder finder;

    private final Long userId = 1L;

    @Test
    void noForfait() {
        List<Forfait> forfaits = finder.findAllForfaits();
        assertTrue(forfaits.isEmpty());
    }

    @Test
    void registerForfait() throws Exception{
        Forfait newForfait = new Forfait(userId, LocalDate.of(2026,02,11), AgeCategory.ADULT, PassType.WEEKLY);
        register.registerForfait(newForfait, userId);
        List<Forfait> fetchedForfait = finder.findForfaitByUserId(userId);
        assertFalse(fetchedForfait.isEmpty());
        assertEquals(newForfait, fetchedForfait.getFirst());
    }

    @Test
    void registerForfaits() throws Exception{
        Forfait newForfait = new Forfait(userId, LocalDate.of(2026,02,11), AgeCategory.ADULT, PassType.WEEKLY);
        register.registerForfait(newForfait, userId);
        List<Forfait> fetchedForfait = finder.findForfaitByUserId(userId);
        assertFalse(fetchedForfait.isEmpty());
        assertEquals(newForfait, fetchedForfait.getFirst());
    }
}
