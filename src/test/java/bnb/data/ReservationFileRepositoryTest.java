package bnb.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import bnb.models.Reservation;
import bnb.models.Host;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {
    static final String SEED_FILE_PATH = "./data/reservation-seed-5309c8e6-1c9b-43a2-a9ab-b5ec23687858.csv";
    static final String TEST_FILE_PATH = "./data/reservations_test/5309c8e6-1c9b-43a2-a9ab-b5ec23687858.csv";
    static final String TEST_DIR_PATH = "./data/reservations_test";
    static final int NEXT_ID = 4;
    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindByHostId() {
        List<Reservation> reservations = repository.findByHostId("5309c8e6-1c9b-43a2-a9ab-b5ec23687858");

        assertNotNull(reservations);
        assertEquals(3, reservations.size());
    }

    @Test
    void shouldReturnEmptyList() {
        List<Reservation> reservations = repository.findByHostId(null);

        assertTrue(reservations.isEmpty());
    }

    @Test
    void shouldFindEmptyList() {
        List<Reservation> reservations = repository.findByHostId(null);

        assertEquals(0, reservations.size());
    }

    @Test
    void shouldAddReservation() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(1));  // tomorrow
        reservation.setEndDate(LocalDate.now().plusDays(2));    // day after
        reservation.setGuestId(1002);
        reservation.setCost(BigDecimal.valueOf(1300));
        reservation.setHostId("5309c8e6-1c9b-43a2-a9ab-b5ec23687858");

        Reservation actual = repository.add(reservation);

        List<Reservation> all = repository.findByHostId("5309c8e6-1c9b-43a2-a9ab-b5ec23687858");

        assertEquals(all.size(), NEXT_ID);
        assertEquals(NEXT_ID, reservation.getId());
    }

    @Test
    void shouldNotAddNull() throws DataException {
        List<Reservation> all = repository.findByHostId("5309c8e6-1c9b-43a2-a9ab-b5ec23687858");
        Reservation doesNotExist = repository.add(null);

        assertEquals(NEXT_ID - 1, all.size()); // unchanged
        assertNull(doesNotExist);
    }


    @Test
    void shouldUpdate() throws DataException {
        Reservation first = repository.findByHostId("5309c8e6-1c9b-43a2-a9ab-b5ec23687858").get(0);

        assertEquals(1, first.getId());
        assertEquals(LocalDate.of(2021, 10, 2), first.getStartDate());
        assertEquals(738, first.getGuestId());
        assertEquals(BigDecimal.valueOf(450), first.getCost().setScale(0, RoundingMode.HALF_EVEN));

        first.setStartDate(first.getStartDate().plusDays(1));
        repository.update(first);

        assertEquals(LocalDate.of(2021, 10, 3), first.getStartDate());

    }

    @Test
    void shouldNotUpdateNull() throws DataException {
        boolean actual = repository.update(null);

        assertFalse(actual);
    }

    @Test
    void shouldNotUpdateIncomplete() throws DataException {
        Reservation fake = new Reservation();

        boolean actual = repository.update(fake);

        assertFalse(actual);
    }

    @Test
    void shouldDelete() throws DataException {
        List<Reservation> all = repository.findByHostId("5309c8e6-1c9b-43a2-a9ab-b5ec23687858");
        Reservation res = all.get(2);

        assertEquals(3, all.size());

        boolean actual = repository.delete(res);
        assertTrue(actual);

        all = repository.findByHostId("5309c8e6-1c9b-43a2-a9ab-b5ec23687858");
        assertEquals(2, all.size());
    }

    @Test
    void shouldNotDelete() {
        List<Reservation> all = repository.findByHostId("5309c8e6-1c9b-43a2-a9ab-b5ec23687858");
        boolean actual = all.remove(null);

        assertFalse(actual);
    }

}