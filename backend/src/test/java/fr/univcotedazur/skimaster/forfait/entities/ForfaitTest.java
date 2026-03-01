package fr.univcotedazur.skimaster.forfait.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ForfaitTest {

    private Long customerId;
    private ForfeitOffer offer;

    @BeforeEach
    void setUp() {
        customerId = 7L;
        offer = new ForfeitOffer(AgeCategory.ADULT, PassType.WEEKLY, 35.0);
    }

    @Test
    void constructorAndGetters() {
        LocalDate date = LocalDate.parse("2026-02-01");
        Forfait forfait = new Forfait(customerId, date, offer);

        assertEquals(customerId, forfait.getCustomerId());
        assertEquals(date, forfait.getValidDate());
        assertEquals(PassType.WEEKLY, forfait.getPassType());
        assertEquals(AgeCategory.ADULT, forfait.getAgeCategory());
    }

    @Test
    void setUserId_updatesCustomer() {
        Forfait forfait = new Forfait(customerId, LocalDate.parse("2026-02-01"), offer);
        Long newCustomerId = 9L;

        forfait.setCustomerId(newCustomerId);

        assertEquals(newCustomerId, forfait.getCustomerId());
    }

    @Test
    void userDisplay_containsPassType() {
        Forfait forfait = new Forfait(customerId, LocalDate.parse("2026-02-01"), offer);
        assertTrue(forfait.userDisplay().contains(PassType.WEEKLY.name()));
    }

    @Test
    void toString_containsMainFields() {
        Forfait forfait = new Forfait(customerId, LocalDate.parse("2026-02-01"), offer);
        ReflectionTestUtils.setField(forfait, "id", 99L);

        String rendered = forfait.toString();

        assertTrue(rendered.contains("Forfait n°: 99"));
        assertTrue(rendered.contains("id user : 7-ADULT"));
        assertTrue(rendered.contains("Type de forfait : WEEKLY"));
        assertTrue(rendered.contains("Date d'expiration : 2026-02-01"));
    }

    @Test
    void equalsAndHashCode_basedOnId() {
        Forfait left = new Forfait(customerId, LocalDate.parse("2026-01-01"), offer);
        Forfait sameId = new Forfait(customerId, LocalDate.parse("2026-01-02"), offer);
        Forfait differentId = new Forfait(customerId, LocalDate.parse("2026-01-03"), offer);

        ReflectionTestUtils.setField(left, "id", 1L);
        ReflectionTestUtils.setField(sameId, "id", 1L);
        ReflectionTestUtils.setField(differentId, "id", 2L);

        assertEquals(left, left);
        assertEquals(left, sameId);
        assertNotEquals(left, differentId);
        assertNotEquals(left, null);
        assertNotEquals(left, "forfait");
        assertEquals(left.hashCode(), sameId.hashCode());
    }

    @Test
    void equals_whenIdsAreNull_returnsFalse() {
        Forfait first = new Forfait(customerId, LocalDate.parse("2026-01-01"), offer);
        Forfait second = new Forfait(customerId, LocalDate.parse("2026-01-01"), offer);

        assertNotEquals(first, second);
    }
}
