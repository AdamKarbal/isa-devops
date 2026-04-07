package fr.univcotedazur.skimaster.admin.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class AdminDTOsTests {
    @Test
    void dtoStoresValuesCorrectly() {
        AdminDisplayMessageDTO dto = new AdminDisplayMessageDTO("Hello", "D1");

        assertEquals("Hello", dto.message());
        assertEquals("D1", dto.displayCode());

        Instant now = Instant.now();
        DisplayAlertDTO displayDto = new DisplayAlertDTO("Alert", "G1", now);

        assertEquals("Alert", displayDto.message());
        assertEquals("G1", displayDto.gateCode());
        assertEquals(now, displayDto.receivedAt());
    }

    @Test
    void dtoEqualityWorks() {
        AdminDisplayMessageDTO a = new AdminDisplayMessageDTO("Hello", "D1");
        AdminDisplayMessageDTO b = new AdminDisplayMessageDTO("Hello", "D1");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        Instant now = Instant.now();

        DisplayAlertDTO c = new DisplayAlertDTO("Alert", "G1", now);
        DisplayAlertDTO d = new DisplayAlertDTO("Alert", "G1", now);

        assertEquals(c, d);
        assertEquals(c.hashCode(), d.hashCode());
    }

    @Test
    void dtoInequalityWorks() {
        Instant now = Instant.now();

        DisplayAlertDTO a = new DisplayAlertDTO("Alert", "G1", now);
        DisplayAlertDTO b = new DisplayAlertDTO("Different", "G1", now);

        assertNotEquals(a, b);
    }

    @Test
    void dtoIsImmutable() {
        Instant now = Instant.now();
        DisplayAlertDTO dto = new DisplayAlertDTO("Alert", "G1", now);

        // Records are immutable: no setters exist
        assertThrows(NoSuchMethodException.class,
                () -> DisplayAlertDTO.class.getDeclaredMethod("setMessage", String.class));
    }

    @Test
    void toStringContainsFieldValues() {
        Instant now = Instant.now();
        DisplayAlertDTO dto = new DisplayAlertDTO("Alert", "G1", now);

        String text = dto.toString();

        assertTrue(text.contains("Alert"));
        assertTrue(text.contains("G1"));
        assertTrue(text.contains(now.toString()));
    }
}
