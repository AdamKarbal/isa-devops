package fr.univcotedazur.skimaster.forfait.component;

import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.forfait.components.ForfaitFinder;
import fr.univcotedazur.skimaster.forfait.dto.ForfaitDTO;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.exceptions.ForfaitIdNotFoundException;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForfaitFinderTest {

    @Mock
    private ForfaitRepository forfaitRepository;

    @Mock
    private CustomerRepository customerRepository;

    private ForfaitFinder forfaitFinder;

    @BeforeEach
    void setUp() {
        forfaitFinder = new ForfaitFinder(forfaitRepository, customerRepository);
    }

    @Test
    void findForfaitByUserId_delegatesToRepository() {
        Long customerId = 42L;
        when(forfaitRepository.findAllByCustomerId(customerId)).thenReturn(List.of());

        List<Forfait> result = forfaitFinder.findForfaitByUserId(customerId);

        assertNotNull(result);
        verify(forfaitRepository).findAllByCustomerId(customerId);
    }

    @Test
    void findAllForfaits_delegatesToRepository() {
        when(forfaitRepository.findAll()).thenReturn(List.of());

        List<Forfait> result = forfaitFinder.findAllForfaits();

        assertNotNull(result);
        verify(forfaitRepository).findAll();
    }

    @Test
    void getAvailablePassTypes_returnsAllEnumValues() {
        List<PassType> passTypes = forfaitFinder.getAvailablePassTypes();
        assertEquals(List.of(PassType.values()), passTypes);
    }

    @Test
    void findForfaitsByCustomerId_whenCustomerDoesNotExist_throwsCustomerIdNotFoundException() {
        Long customerId = 10L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThrows(CustomerIdNotFoundException.class, () -> forfaitFinder.findForfaitsByCustomerId(customerId));
    }

    @Test
    void findForfaitsByCustomerId_whenNoForfaits_returnsEmptyList() {
        Long customerId = 10L;
        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(forfaitRepository.findAllByCustomerId(customerId)).thenReturn(List.of());

        List<ForfaitDTO> result = forfaitFinder.findForfaitsByCustomerId(customerId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findForfaitsByCustomerId_returnsMappedDtos() {
        Long customerId = 20L;
        ForfeitOffer offer = new ForfeitOffer(AgeCategory.ADULT, PassType.WEEKLY, 50.0);
        Forfait first = new Forfait(customerId, LocalDate.parse("2026-01-10"), offer);
        Forfait second = new Forfait(customerId, LocalDate.parse("2026-01-11"), offer);
        org.springframework.test.util.ReflectionTestUtils.setField(first, "id", 55L);
        org.springframework.test.util.ReflectionTestUtils.setField(second, "id", 56L);

        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(forfaitRepository.findAllByCustomerId(customerId)).thenReturn(List.of(first, second));

        List<ForfaitDTO> result = forfaitFinder.findForfaitsByCustomerId(customerId);

        assertEquals(2, result.size());
        assertEquals(55L, result.getFirst().id());
        assertEquals(56L, result.get(1).id());
        assertEquals(customerId, result.getFirst().customerId());
        assertEquals(PassType.WEEKLY.name(), result.getFirst().passType());
        assertEquals(AgeCategory.ADULT.name(), result.getFirst().ageCategory());
    }

    @Test
    void getForfait_whenCustomerDoesNotExist_throwsCustomerIdNotFoundException() {
        Long customerId = 10L;
        Long forfaitId = 100L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThrows(CustomerIdNotFoundException.class, () -> forfaitFinder.getForfait(customerId, forfaitId));
    }

    @Test
    void getForfait_whenForfaitDoesNotExist_throwsForfaitIdNotFoundException() {
        Long customerId = 10L;
        Long forfaitId = 100L;
        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(forfaitRepository.findByIdAndCustomerId(forfaitId, customerId)).thenReturn(Optional.empty());

        assertThrows(ForfaitIdNotFoundException.class, () -> forfaitFinder.getForfait(customerId, forfaitId));
    }

    @Test
    void getForfait_returnsMappedForfaitDto() {
        ForfeitOffer offer = new ForfeitOffer(AgeCategory.ADULT, PassType.WEEKLY, 50.0);
        Forfait forfait = new Forfait(20L, LocalDate.parse("2026-01-10"), offer);
        org.springframework.test.util.ReflectionTestUtils.setField(forfait, "id", 55L);

        when(customerRepository.existsById(20L)).thenReturn(true);
        when(forfaitRepository.findByIdAndCustomerId(55L, 20L)).thenReturn(Optional.of(forfait));

        ForfaitDTO dto = forfaitFinder.getForfait(20L, 55L);

        assertEquals(55L, dto.id());
        assertEquals(20L, dto.customerId());
        assertEquals(PassType.WEEKLY.name(), dto.passType());
        assertEquals(AgeCategory.ADULT.name(), dto.ageCategory());
        assertEquals(LocalDate.parse("2026-01-10"), dto.validDate());
    }
}
