package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.cashier.interfaces.InvoiceCreator;
import fr.univcotedazur.skimaster.cashier.interfaces.InvoiceFinder;
import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerFinder;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.interfaces.FFinder;
import fr.univcotedazur.skimaster.gate.connectors.TurnstileProxy;
import fr.univcotedazur.skimaster.gate.entities.Passage;
import fr.univcotedazur.skimaster.gate.interfaces.AccessTreatment;
import fr.univcotedazur.skimaster.gate.repositories.PassageRepository;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
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
    private final InvoiceFinder invoiceFinder;
    private final CustomerFinder customerFinder;
    private final InvoiceCreator invoiceCreator;
    private final OrderRepository orderRepository;
    private final PassageRepository passageRepository;
    private final TurnstileProxy turnstileProxy;

    public PassService(FFinder forfaitFinder,
                       InvoiceFinder invoiceFinder,
                       CustomerFinder customerFinder,
                       InvoiceCreator invoiceCreator,
                       OrderRepository orderRepository,
                       PassageRepository passageRepository,
                       TurnstileProxy turnstileProxy) {
        this.forfaitFinder = forfaitFinder;
        this.invoiceFinder = invoiceFinder;
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
                .map(Forfait::getCustomerId)
                .distinct()
                .toList();

        LOG.info("LOGIC: Pushing {} IDs to Portier for broadcast", authorizedIds.size());
        turnstileProxy.syncAllGates(authorizedIds);
    }

    @Transactional
    public void collectAndProcessAllPassages() {
        List<Passage> allPassengers = turnstileProxy.collectPassagesFromAllGates();
        LOG.info("LOGIC: Received {} total passages from Portier", allPassengers.size());
        processReceivedPassages(allPassengers);
    }

    @Override
    @Transactional
    public void processReceivedPassages(List<Passage> passages) {
        LOG.info("START: Processing {} passages from collector", passages.size());
        for (Passage passage : passages) {
            processIncomingPassage(passage);
        }
        LOG.info("END: Processing passages finished");
    }

    private void processIncomingPassage(Passage passage) {
        LocalDateTime now = LocalDateTime.now();
        passageRepository.save(new Passage(passage.getCustomerId(), now,passage.getGatePassedBy()));
        try {
            Customer customer = customerFinder.retrieveCustomer(passage.getCustomerId());
            if (customer.isHasSuperCarte()) {
                LocalDateTime oneHourAgo = now.minusHours(1);
                boolean hasInvoiceRecently = invoiceFinder.findInvoicesByCustomerId(customer.getId()).stream()
                        .filter(inv -> inv.getAmount() == 15.0)
                        .anyMatch(inv -> inv.getPaymentDate().isAfter(oneHourAgo));
                boolean hasOrderRecently = orderRepository.existsRecentOrderForSuperCarte(customer.getId(), 15.0);

                if (!hasInvoiceRecently && !hasOrderRecently) {
                    handleSuperCarteBilling(customer);
                    LOG.info("SUPERCARTE: Facturation déclenchée pour {}", customer.getName());
                } else {
                    LOG.info("SUPERCARTE: Passage déjà traité ou facturé pour {}", customer.getName());
                }
            }
        } catch (CustomerIdNotFoundException e) {
            LOG.error("Client inconnu : {}", passage.getCustomerId());
        }
    }


    private void handleSuperCarteBilling(Customer customer) {
        Order superOrder = new Order(customer, new HashSet<>(), 15.0);
        superOrder = orderRepository.save(superOrder);
        invoiceCreator.processOrderPayment(superOrder);
    }
}
