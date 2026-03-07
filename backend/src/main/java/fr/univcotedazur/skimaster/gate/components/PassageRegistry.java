package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.gate.repositories.PassageFinder;
import fr.univcotedazur.skimaster.gate.repositories.PassageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class PassageRegistry implements PassageFinder {
    private final PassageRepository passageRepository;
    public PassageRegistry(PassageRepository passageRepository){
        this.passageRepository=passageRepository;
    }

    public List<Passage> findAllPassagesByCustomer(Long customerId){
        return passageRepository.findByCustomerId(customerId);
    }

    private List<Passage> findByDate(LocalDate localDate){
        return passageRepository.findByDate(localDate);
    }

    public List<Passage> findTodayPassages(){
        return findByDate(LocalDate.now());
    }

}
