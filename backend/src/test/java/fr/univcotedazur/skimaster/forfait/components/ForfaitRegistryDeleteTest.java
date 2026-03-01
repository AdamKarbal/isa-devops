package fr.univcotedazur.skimaster.forfait.components;

import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.exceptions.ForfaitIdNotFoundException;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForfaitRegistryDeleteTest {

    @Mock
    private ForfaitRepository forfaitRepository;

    private ForfaitRegistry forfaitRegistry;

    @BeforeEach
    void setUp() {
        forfaitRegistry = new ForfaitRegistry(forfaitRepository);
    }

    @Test
    void deleteForfait_whenForfaitExists_deletesIt() {
        Long customerId = 12L;
        Long forfaitId = 45L;
        Forfait forfait = new Forfait();
        when(forfaitRepository.findByIdAndCustomerId(forfaitId, customerId)).thenReturn(Optional.of(forfait));

        forfaitRegistry.deleteForfait(customerId, forfaitId);

        verify(forfaitRepository).delete(forfait);
    }

    @Test
    void deleteForfait_whenForfaitDoesNotExist_throwsForfaitIdNotFoundException() {
        Long customerId = 12L;
        Long forfaitId = 45L;
        when(forfaitRepository.findByIdAndCustomerId(forfaitId, customerId)).thenReturn(Optional.empty());

        assertThrows(ForfaitIdNotFoundException.class, () -> forfaitRegistry.deleteForfait(customerId, forfaitId));
    }
}
