package bnb.data;

import bnb.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    private static final String SEED_FILE_PATH = "./data/guests-seed.csv";
    private static final String TEST_FILE_PATH = "./data/guests-test.csv";
    private final int GUEST_COUNT = 20;
    private final GuestFileRepository repository = new GuestFileRepository(TEST_FILE_PATH);

//    @BeforeEach
//    public void setup() throws IOException {
//        Path seedPath = Paths.get(SEED_FILE_PATH);
//        Path testPath = Paths.get(TEST_FILE_PATH);
//        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
//    }

    @Test
    void shouldFindAll() {
        List<Guest> all = repository.findAll();

        assertNotNull(all);
        assertEquals(GUEST_COUNT, all.size());

        assertEquals("Elizabeth", all.get(0).getFirstName());
        assertEquals("NJ", all.get(14).getState());
    }

    @Test
    void shouldFindGuestById() {
        Guest newGuest = repository.findById(10);

        // 10,Isabel,Ganter,iganter9@privacy.gov.au,(915) 5895326,TX

        assertNotNull(newGuest);
        assertEquals(10, newGuest.getGuestId());
        assertEquals("Isabel", newGuest.getFirstName());
        assertEquals("iganter9@privacy.gov.au", newGuest.getEmail());


    }

    @Test
    void shouldNotFindFakeGuest() {
        Guest newGuest = repository.findById(21);

        assertNull(newGuest);
    }

}
