package fr.univcotedazur.skimaster.forfait.components;

import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.interfaces.FFinder;
import fr.univcotedazur.skimaster.forfait.interfaces.FRegister;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ForfaitRegistry implements FFinder, FRegister {
    private final ForfaitRepository repository;

    public ForfaitRegistry(ForfaitRepository repository){
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Forfait> findForfaitByUserId(Long customerId){
        return this.repository.findByUserId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Forfait> findAllForfaits(){
        return this.repository.findAll();
    }

    @Override
    @Transactional
    public void registerForfait(Forfait forfait, Long userId){
        forfait.setUserId(userId);
        this.repository.save(forfait);
    }

    @Override
    @Transactional
    public void registerForfaits(List<Forfait> forfaits, Long userId){
        for(Forfait f : forfaits){
            f.setUserId(userId);
        }
        this.repository.saveAll(forfaits);
    }
}
