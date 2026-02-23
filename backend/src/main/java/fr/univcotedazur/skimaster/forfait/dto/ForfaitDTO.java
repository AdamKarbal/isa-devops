package fr.univcotedazur.skimaster.forfait.dto;

import fr.univcotedazur.skimaster.customer.entities.Customer;

import java.time.LocalDate;

public record ForfaitDTO(
        Long id,
        Customer customer,
        String passType,
        String ageCategory,
        LocalDate validDate
) {}
