package fr.univcotedazur.skimaster.customer.components;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.customer.exceptions.AlreadyExistingCustomerException;
import fr.univcotedazur.skimaster.customer.exceptions.CustomerIdNotFoundException;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerFinder;
import fr.univcotedazur.skimaster.customer.interfaces.CustomerRegistration;
import fr.univcotedazur.skimaster.customer.repositories.CustomerRepository;
import fr.univcotedazur.skimaster.forfait.components.ForfaitRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerRegistry implements CustomerRegistration, CustomerFinder {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerRegistry.class);
    private final CustomerRepository customerRepository;

    public CustomerRegistry(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public Customer register(String name, String creditCard)
            throws AlreadyExistingCustomerException {
        if (findByName(name).isPresent())
            throw new AlreadyExistingCustomerException(name);
        LOG.info("[BASE-DE-DONNEES][Client] Tentative d'enregistrement du client : {}", name);
        Customer newcustomer = new Customer(name, creditCard);
        Customer saved = customerRepository.save(newcustomer);
        LOG.info("[BASE-DE-DONNEES][Client] Client persisté avec l'ID généré : {}", saved.getId());
        return customerRepository.save(newcustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findByName(String name) {
        return customerRepository.findCustomerByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer retrieveCustomer(Long customerId) throws CustomerIdNotFoundException {
        return findById(customerId).orElseThrow(() -> new CustomerIdNotFoundException(customerId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

}
