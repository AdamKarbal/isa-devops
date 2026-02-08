package fr.univcotedazur.skimaster.forfait.interfaces;

import fr.univcotedazur.skimaster.forfait.dto.ForfaitDTO;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.PassType;

import java.util.List;

public interface FFinder {
    List<Forfait> findForfaitByUserId(Long customerId);
    List<Forfait> findAllForfaits();
    List<PassType> getAvailablePassTypes();
    List<ForfaitDTO> findForfaitsByCustomerId(Long customerId);
}
