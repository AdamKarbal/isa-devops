package fr.univcotedazur.skimaster.cli.model;

import java.time.LocalDate;

public class CliForfait {
    private Long id;
    private Long customerId;
    private String passType;
    private String ageCategory;
    private LocalDate validDate;

    public CliForfait() {}

    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public String getPassType() { return passType; }
    public String getAgeCategory() { return ageCategory; }
    public LocalDate getValidDate() { return validDate; }
}
