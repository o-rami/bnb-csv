package bnb.domain;

import bnb.data.GuestRepository;
import bnb.data.GuestRepositoryDouble;
import bnb.models.Guest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    private final GuestRepository repository = new GuestRepositoryDouble();

    @Test
    void shouldFindAll() {
        List<Guest> all = repository.findAll();

        assertEquals(4, all.size());
        assertEquals("Nye", all.get(2).getFirstName());
    }

    @Test
    void shouldFindById() {
        Guest guest = repository.findById(2);

        assertNotNull(guest);
        assertEquals("Nye", guest.getFirstName());
    }

    @Test
    void shouldNotFindIdZero() {
        Guest guest = repository.findById(0);

        assertNull(guest);
    }

    @Test
    void shouldNotFindNegativeId() {
        Guest guest = repository.findById(-2);

        assertNull(guest);
    }

}