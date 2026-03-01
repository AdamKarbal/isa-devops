package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.cashier.interfaces.InvoiceCreator;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerFinder;
import fr.univcotedazur.skimaster.forfait.interfaces.FFinder;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.gate.interfaces.AccessTreatment;
import fr.univcotedazur.skimaster.gate.repositories.PassageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class PassService implements AccessTreatment {

    private static final Logger LOG = LoggerFactory.getLogger(PassService.class);

    private final FFinder forfaitFinder;
    private final CustomerFinder customerFinder;
    private final InvoiceCreator invoiceCreator;
    private final OrderRepository orderRepository;
    private final PassageRepository passageRepository;
    private final TurnstileProxy turnstileProxy;

    public PassService(FFinder forfaitFinder, CustomerFinder customerFinder,
                       InvoiceCreator invoiceCreator, OrderRepository orderRepository,
                       PassageRepository passageRepository, TurnstileProxy turnstileProxy) {
        this.forfaitFinder = forfaitFinder;
        this.customerFinder = customerFinder;
        this.invoiceCreator = invoiceCreator;
        this.orderRepository = orderRepository;
        this.passageRepository = passageRepository;
        this.turnstileProxy = turnstileProxy;
    }

    @Override
    public void pushAuthorizedBadges() {
        List<Long> authorizedIds = forfaitFinder.findAllForfaits().stream()
                .filter(f -> !f.getValidDate().isBefore(LocalDate.now()))
                .map(f -> f.getCustomerId())
                .toList();
        turnstileProxy.syncAuthorizedCards(authorizedIds);
    }


    @Override
    @Transactional
    public void processReceivedPassages(List<Long> nfcCardIds) {
        LOG.info("START: Processing {} passages", nfcCardIds.size());
        for (Long id : nfcCardIds) {
            processIncomingPassage(id);
        }
        LOG.info("END: Processing passages finished");
    }

    private void processIncomingPassage(Long nfcCardId) {
        LocalDateTime now = LocalDateTime.now();
        passageRepository.save(new Passage(nfcCardId, now));

        try {
            Customer customer = customerFinder.retrieveCustomer(nfcCardId);
            LOG.info("Customer found: {} (SuperCarte: {})", customer.getName(), customer.isHasSuperCarte());

            if (customer.isHasSuperCarte()) {
                handleSuperCarteBilling(customer);
            } else {
                LOG.info("No SuperCarte for {}", customer.getName());
            }
        } catch (CustomerIdNotFoundException e) {
            LOG.error("{} unknown in system", nfcCardId);
        }
    }

    public void processPassages(List<Long> nfcCardIds) {
        for (Long id : nfcCardIds) {
            processIncomingPassage(id);
        }
    }

    //MODIFI2 MIEU HANDLE COMPARER PAR HEURE ET FAIRE UNE ORDER AVEC PRODUCT PAS UN PRIX FIXE
    private void handleSuperCarteBilling(Customer customer) {
        boolean alreadyBilled = orderRepository.findAll().stream()
                .anyMatch(o -> o.getCustomer().getId().equals(customer.getId()) && o.getPrice() == 15.0);

        if (!alreadyBilled) {
            Order superOrder = new Order(customer, new HashSet<>(), 15.0);
            superOrder = orderRepository.save(superOrder);
            invoiceCreator.processOrderPayment(superOrder);
            LOG.info("SUPERCARTE: Commande de 15 euros créée pour {}", customer.getName());
        }
    }

    private void handleSuperCarteLogic(Customer customer, LocalDateTime now) {
        List<Passage> todaysPassages = passageRepository.findByCustomerIdAndDate(customer.getId(), now.toLocalDate());
        LocalDateTime firstPassageTime = todaysPassages.get(0).getOccurrenceDate();
        handleSuperCarteBilling(customer);

    }


}
