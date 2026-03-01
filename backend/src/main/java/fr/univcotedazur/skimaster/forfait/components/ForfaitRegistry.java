package fr.univcotedazur.skimaster.forfait.components;

import fr.univcotedazur.skimaster.forfait.dto.ForfaitDTO;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.exceptions.ForfaitIdNotFoundException;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.interfaces.FFinder;
import fr.univcotedazur.skimaster.forfait.interfaces.FRegister;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ForfaitRegistry implements FRegister, FFinder {
    private final ForfaitRepository repository;

    public ForfaitRegistry(ForfaitRepository repository){
        this.repository = repository;
    }

    @Override
    @Transactional
    public void registerForfait(Forfait forfait, Long userId){
        forfait.setCustomerId(userId);
        this.repository.save(forfait);
    }

    @Override
    @Transactional
    public void registerForfaits(List<Forfait> forfaits, Long userId){
        for(Forfait f : forfaits){
            f.setCustomerId(userId);
        }
        this.repository.saveAll(forfaits);
    }

    @Override
    @Transactional
    public void deleteForfait(Long customerId, Long forfaitId) {
        Forfait forfait = repository.findByIdAndCustomerId(forfaitId, customerId)
                .orElseThrow(() -> new ForfaitIdNotFoundException(forfaitId));
        repository.delete(forfait);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Forfait> findAllForfaits(){
        return this.repository.findAll();
    }

    @Override
    public List<PassType> getAvailablePassTypes() {
        return Arrays.asList(PassType.values());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ForfaitDTO> findForfaitsByCustomerId(Long customerId) {
        return repository.findAllByCustomerId(customerId).stream()
                .map(this::toDto)
                .toList();
    }

    private ForfaitDTO toDto(Forfait forfait) {
        return new ForfaitDTO(
                forfait.getId(),
                forfait.getCustomerId(),
                forfait.getPassType().name(),
                forfait.getAgeCategory().name(),
                forfait.getValidDate()
        );
    }
}
