package fr.univcotedazur.skimaster.forfait.component;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.forfait.components.ForfaitFinder;
import fr.univcotedazur.skimaster.forfait.dto.ForfaitDTO;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.ForfaitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.exceptions.ForfaitIdNotFoundException;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitOfferRepository;
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

    @Autowired ForfaitFinder forfaitFinder;
    @Autowired CustomerRepository customerRepository;
    @Autowired ForfaitRepository forfaitRepository;
    @Autowired
    ForfaitOfferRepository forfaitOfferRepository;

    @Test
    void getForfait_customerNotFound_throws() {
        assertThrows(CustomerIdNotFoundException.class,
                () -> forfaitFinder.getForfait(999999L, 1L));
    }

    @Test
    void getForfait_forfaitNotFound_throws() {
        Customer c = customerRepository.save(new Customer("C-" + java.util.UUID.randomUUID(), CC_OK));
        assertThrows(ForfaitIdNotFoundException.class,
                () -> forfaitFinder.getForfait(c.getId(), 999999L));
    }

    @Test
    void getForfait_returnsDto_whenOwnedByCustomer() {
        Customer c = customerRepository.save(new Customer("C-" + java.util.UUID.randomUUID(), CC_OK));

        ForfaitOffer offer = forfaitOfferRepository.save(
                new ForfaitOffer(AgeCategory.ADULT, PassType.WEEKLY, 42.0)
        );

        LocalDate date = LocalDate.of(2026, 2, 11);
        Forfait f = forfaitRepository.save(new Forfait(c.getId(), date, offer));

        ForfaitDTO dto = forfaitFinder.getForfait(c.getId(), f.getId());

        assertEquals(f.getId(), dto.id());
        // adapte selon ton DTO réel :
        assertEquals(c.getId(), dto.customerId());
        assertEquals(PassType.WEEKLY.name(), dto.passType());
        assertEquals(AgeCategory.ADULT.name(), dto.ageCategory());
        assertEquals(date, dto.validDate());
    }

    @Test
    void getForfait_throws_ifForfaitBelongsToOtherCustomer() {
        Customer c1 = customerRepository.save(new Customer("C1-" + java.util.UUID.randomUUID(), CC_OK));
        Customer c2 = customerRepository.save(new Customer("C2-" + java.util.UUID.randomUUID(), CC_OK));

        ForfaitOffer offer = forfaitOfferRepository.save(
                new ForfaitOffer(AgeCategory.ADULT, PassType.WEEKLY, 42.0)
        );

        Forfait f = forfaitRepository.save(new Forfait(c2.getId(), LocalDate.of(2026, 2, 11), offer));

        assertThrows(ForfaitIdNotFoundException.class,
                () -> forfaitFinder.getForfait(c1.getId(), f.getId()));
    }

    @Test
    void findForfaitByUserId_returnsOnlyThisCustomersForfaits() {
        Customer c1 = customerRepository.save(new Customer("C1-" + java.util.UUID.randomUUID(), CC_OK));
        Customer c2 = customerRepository.save(new Customer("C2-" + java.util.UUID.randomUUID(), CC_OK));

        ForfaitOffer oFull = forfaitOfferRepository.save(new ForfaitOffer(AgeCategory.ADULT, PassType.FULL, 50.0));
        ForfaitOffer oWeeklyChild = forfaitOfferRepository.save(new ForfaitOffer(AgeCategory.CHILD, PassType.WEEKLY, 30.0));
        ForfaitOffer oSeasonal = forfaitOfferRepository.save(new ForfaitOffer(AgeCategory.ADULT, PassType.SEASONAL, 400.0));

        forfaitRepository.save(new Forfait(c1.getId(), LocalDate.now(), oFull));
        forfaitRepository.save(new Forfait(c1.getId(), LocalDate.now().plusDays(1), oWeeklyChild));
        forfaitRepository.save(new Forfait(c2.getId(), LocalDate.now(), oSeasonal));

        var res = forfaitFinder.findForfaitByUserId(c1.getId());
        assertEquals(2, res.size());
        assertTrue(res.stream().allMatch(f -> f.getUserId().equals(c1.getId())));
    }
}
