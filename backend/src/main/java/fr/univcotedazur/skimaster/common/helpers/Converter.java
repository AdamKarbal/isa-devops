package fr.univcotedazur.skimaster.common.helpers;

import fr.univcotedazur.skimaster.forfait.entities.PassType;

import java.time.LocalDate;

public class Converter {
    public static LocalDate computeValidDate(PassType passType) {
        LocalDate now = LocalDate.now();
        return switch (passType) {
            case HALFADAY -> now;
            case DAILY, FULL -> now.plusDays(1);
            case WEEKLY -> now.plusWeeks(1);
            case SEASONAL -> now.plusMonths(4);
            case UNLIMITED -> now.plusYears(100);
        };
    }
}
