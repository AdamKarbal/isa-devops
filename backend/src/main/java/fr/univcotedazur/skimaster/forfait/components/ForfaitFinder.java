package fr.univcotedazur.skimaster.forfait.components;

import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.forfait.dto.ForfaitDTO;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.exceptions.ForfaitIdNotFoundException;
import fr.univcotedazur.skimaster.forfait.interfaces.FFinder;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitRepository;
import fr.univcotedazur.skimaster.order.dto.OrderDTO;
import fr.univcotedazur.skimaster.order.dto.OrderLineDTO;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.exceptions.OrderIdNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ForfaitFinder implements FFinder {

    private final ForfaitRepository forfaitRepository;
    private final CustomerRepository customerRepository;

    public ForfaitFinder(ForfaitRepository forfaitRepository, CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.forfaitRepository = forfaitRepository;
    }

    public List<ForfaitDTO> findForfaitsByCustomerId(Long customerId) {
        return null;
    }

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

    public ForfaitDTO getForfait(Long customerId, Long forfaitId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerIdNotFoundException(customerId);
        }

        Forfait f = forfaitRepository.findByIdAndCustomerId(customerId, forfaitId)
                .orElseThrow(() -> new ForfaitIdNotFoundException(forfaitId));

        return toDto(f);
    }

    private ForfaitDTO toDto(Forfait f) {
        return new ForfaitDTO(
                f.getId(),
                f.getCustomer().getId(),
                f.getPassType().name(),
                f.getAgeCategory().name(),
                f.getValidDate()
        );
    }
}
