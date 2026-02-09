package fr.univcotedazur.skimaster.forfait.repositories;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
public class ForfaitRepositoryTest {

    private final Long userId = 1L;


    @Autowired
    private ForfaitRepository repository;

    @Test
    void testIdGenerationAndUnicity() {
        Forfait forfait = new Forfait(userId, LocalDate.of(2026,02,11), AgeCategory.ADULT, PassType.WEEKLY);
        Assertions.assertNull(forfait.getId());
        repository.saveAndFlush(forfait); // save in the persistent context and force saving in the DB (thus ensuring validation by Hibernate)
        Assertions.assertNotNull(forfait.getId());
    }

    @Test
    void testFindForfaitByUserId(){
        Forfait forfait = new Forfait(userId, LocalDate.of(2026,02,11), AgeCategory.ADULT, PassType.WEEKLY);
        repository.saveAndFlush(forfait);
        Assertions.assertEquals(repository.findByUserId(userId), List.of(forfait));
    }

    @Test
    void testNullUserId() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> repository.saveAndFlush(new Forfait(null, LocalDate.of(2026,02,11), AgeCategory.ADULT, PassType.WEEKLY)));
    }

    @Test
    void testNullVDate() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> repository.saveAndFlush(new Forfait(userId, null, AgeCategory.ADULT, PassType.WEEKLY)));
    }

    @Test
    void testNullAgeCategory() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> repository.saveAndFlush(new Forfait(userId, LocalDate.of(2026,02,11), null, PassType.WEEKLY)));
    }

    @Test
    void testNullPassType() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> repository.saveAndFlush(new Forfait(userId, LocalDate.of(2026,02,11), AgeCategory.ADULT, null)));
    }
}
