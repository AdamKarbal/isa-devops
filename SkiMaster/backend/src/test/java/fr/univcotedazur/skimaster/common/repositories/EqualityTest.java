package fr.univcotedazur.skimaster.common.repositories;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EqualityTest {

    private Customer john;

    @BeforeEach
    void setup() {
        john = new Customer("john", "1234567890");
    }

    @Test
    void testCustomerEquals() {
        assertEquals(john, john);
        Customer otherJohn = new Customer("john", "1234567890");
        assertEquals(john, otherJohn);
        assertEquals(otherJohn, john);
    }

    @Test
    void testCustomerNotEquals() {
        assertEquals(john, john);
        Customer otherJohn = new Customer("johnn", "1234567890");
        assertNotEquals(john, otherJohn);
        assertNotEquals(otherJohn, john);
        Customer anotherJohn = new Customer("john", "1234567891");
        assertNotEquals(john, anotherJohn);
        assertNotEquals(anotherJohn, john);
    }


}
