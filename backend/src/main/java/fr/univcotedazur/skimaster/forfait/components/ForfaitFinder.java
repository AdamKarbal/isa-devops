package fr.univcotedazur.skimaster.forfait.components;

import fr.univcotedazur.skimaster.forfait.dto.ForfaitDTO;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.interfaces.FFinder;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class ForfaitFinder implements FFinder {

    private final ForfaitRepository forfaitRepository;

    public ForfaitFinder(ForfaitRepository forfaitRepository) {
        this.forfaitRepository = forfaitRepository;
    }

    // NOUVELLE méthode pour ton use-case (DTO + status)
    public List<ForfaitDTO> findForfaitsByCustomerId(Long customerId) {
        LocalDate today = LocalDate.now();

        return forfaitRepository.findAllByCustomerId(customerId).stream()
                .map(f -> new ForfaitDTO(
                        f.getId(),
                        f.getValidDate(),
                        f.getAgeCategory(),
                        f.getPassType(),
                        f.getValidDate().isBefore(today) ? "EXPIRED" : "ACTIVE"
                ))
                .toList();
    }

    // méthodes de ton interface (si tu les gardes)
    @Override
    public List<Forfait> findForfaitByUserId(Long customerId) {
        return forfaitRepository.findAllByCustomerId(customerId);
    }

    @Override
    public List<Forfait> findAllForfaits() {
        return forfaitRepository.findAll();
    }

    @Override
    public List<PassType> getAvailablePassTypes() {
        return Arrays.asList(PassType.values());
    }
}
