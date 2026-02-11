package fr.univcotedazur.skimaster.forfait.component;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.forfait.components.ForfaitFinder;
import fr.univcotedazur.skimaster.forfait.dto.ForfaitDTO;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.exceptions.ForfaitIdNotFoundException;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ForfaitFinderTest {

    private static final String CC_OK = "1234567890";
    @Autowired
    ForfaitFinder forfaitFinder;
    @Autowired CustomerRepository customerRepository;
    @Autowired ForfaitRepository forfaitRepository;

    @Test
    void getForfait_customerNotFound_throws() {
        assertThrows(CustomerIdNotFoundException.class, () -> forfaitFinder.getForfait(999999L, 1L));
    }

    @Test
    void getForfait_forfaitNotFound_throws() {
        Customer c = customerRepository.save(new Customer("C-" + java.util.UUID.randomUUID(), CC_OK));
        assertThrows(ForfaitIdNotFoundException.class, () -> forfaitFinder.getForfait(c.getId(), 999999L));
    }

    @Test
    void getForfait_returnsDto_whenOwnedByCustomer() {
        Customer c = customerRepository.save(new Customer("C-" + java.util.UUID.randomUUID(), CC_OK));

        LocalDate date = LocalDate.of(2026, 2, 11);
        Forfait f = forfaitRepository.save(new Forfait(c, date, AgeCategory.ADULT, PassType.WEEKLY));

        ForfaitDTO dto = forfaitFinder.getForfait(c.getId(), f.getId());

        assertEquals(f.getId(), dto.id());
        assertEquals(c.getId(), dto.customerId());
        assertEquals(PassType.WEEKLY.name(), dto.passType());
        assertEquals(AgeCategory.ADULT.name(), dto.ageCategory());
        assertEquals(date, dto.validDate());
    }

    @Test
    void getForfait_throws_ifForfaitBelongsToOtherCustomer() {
        Customer c1 = customerRepository.save(new Customer("C1-" + java.util.UUID.randomUUID(), CC_OK));
        Customer c2 = customerRepository.save(new Customer("C2-" + java.util.UUID.randomUUID(), CC_OK));

        Forfait f = forfaitRepository.save(new Forfait(c2, LocalDate.of(2026, 2, 11), AgeCategory.ADULT, PassType.WEEKLY));

        assertThrows(ForfaitIdNotFoundException.class, () -> forfaitFinder.getForfait(c1.getId(), f.getId()));
    }

    @Test
    void findForfaitByUserId_returnsOnlyThisCustomersForfaits() {
        Customer c1 = customerRepository.save(new Customer("C1-" + java.util.UUID.randomUUID(), CC_OK));
        Customer c2 = customerRepository.save(new Customer("C2-" + java.util.UUID.randomUUID(), CC_OK));

        forfaitRepository.save(new Forfait(c1, LocalDate.now(), AgeCategory.ADULT, PassType.FULL));
        forfaitRepository.save(new Forfait(c1, LocalDate.now().plusDays(1), AgeCategory.CHILD, PassType.WEEKLY));
        forfaitRepository.save(new Forfait(c2, LocalDate.now(), AgeCategory.ADULT, PassType.SEASONAL));

        var res = forfaitFinder.findForfaitByUserId(c1.getId());
        assertEquals(2, res.size());
        assertTrue(res.stream().allMatch(f -> f.getCustomer().getId().equals(c1.getId())));
    }
}
