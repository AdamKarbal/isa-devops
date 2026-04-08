package fr.univcotedazur.skimaster.forfait.entities;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class ForfaitOfferTest {

    @Test
    void constructorAndGetters() {
        ForfeitOffer offer = new ForfeitOffer(AgeCategory.ADULT, PassType.FULL, 42.5);

        assertEquals(AgeCategory.ADULT, offer.getAgeCategory());
        assertEquals(PassType.FULL, offer.getPassType());
        assertEquals(42.5, offer.getPrice());
    }

    @Test
    void equalsAndHashCode() {
        ForfeitOffer offer = new ForfeitOffer(AgeCategory.CHILD, PassType.HALFADAY, 18.0);
        ForfeitOffer sameId = new ForfeitOffer(AgeCategory.ADULT, PassType.SEASONAL, 120.0);
        ForfeitOffer differentId = new ForfeitOffer(AgeCategory.ADULT, PassType.WEEKLY, 60.0);

        ReflectionTestUtils.setField(offer, "id", 1L);
        ReflectionTestUtils.setField(sameId, "id", 1L);
        ReflectionTestUtils.setField(differentId, "id", 2L);

        assertEquals(offer, offer);
        assertEquals(offer, sameId);
        assertNotEquals(offer, differentId);
        assertNotEquals(offer, null);
        assertNotEquals(offer, "not an offer");
        assertEquals(31, offer.hashCode());
    }

    @Test
    void equalsWithNullIds_returnsFalse() {
        ForfeitOffer first = new ForfeitOffer(AgeCategory.ADULT, PassType.FULL, 20.0);
        ForfeitOffer second = new ForfeitOffer(AgeCategory.ADULT, PassType.FULL, 20.0);

        assertNotEquals(first, second);
    }
}
