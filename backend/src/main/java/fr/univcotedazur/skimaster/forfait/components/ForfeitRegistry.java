package fr.univcotedazur.skimaster.forfait.components;

import fr.univcotedazur.skimaster.forfait.entities.Forfeit;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.interfaces.FFinder;
import fr.univcotedazur.skimaster.forfait.interfaces.FRegister;
import fr.univcotedazur.skimaster.forfait.repositories.ForfeitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ForfeitRegistry implements FFinder, FRegister {
    private final ForfeitRepository repository;

    public ForfeitRegistry(ForfeitRepository repository){
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Forfeit> findForfaitByUserId(Long customerId){
        return this.repository.findByUserId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Forfeit> findAllForfaits(){
        return this.repository.findAll();
    }

    @Override
    @Transactional
    public void registerForfait(Forfeit forfeit, Long userId){
        forfeit.setUserId(userId);
        this.repository.save(forfeit);
    }

    @Override
    @Transactional
    public void registerForfaits(List<Forfeit> forfeits, Long userId){
        for(Forfeit f : forfeits){
            f.setUserId(userId);
        }
        this.repository.saveAll(forfeits);
    }

    @Override
    public List<PassType> getAvailablePassTypes(){
        return Arrays.asList(PassType.values());
    }


}
