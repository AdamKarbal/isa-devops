package fr.univcotedazur.skimaster.forfait.components;

import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.interfaces.FRegister;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ForfaitRegistry implements FRegister {
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
}
