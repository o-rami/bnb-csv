package bnb.data;

import bnb.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


class HostFileRepositoryTest {

    private final int HOST_COUNT = 20;
    private final String SEED_FILE_PATH = "data/hosts-seed.csv";
    private final String TEST_FILE_PATH = "data/hosts-test.csv";
    HostRepository repository = new HostFileRepository(TEST_FILE_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        List<Host> all = repository.findAll();

        assertNotNull(all);
        assertEquals(HOST_COUNT, all.size());
        assertEquals("Moorcroft", all.get(19).getLastName());
        assertEquals("jcroneya@noaa.gov", all.get(10).getEmail());
    }

    @Test
    void shouldFindHostById() {
        Host host = repository.findById("86bed322-2c2f-4254-8ea3-4821e90c6809");

        assertNotNull(host);
        assertEquals("(561) 6295366", host.getPhoneNumber());
        assertEquals("Boca Raton", host.getCity());
        assertEquals("FL", host.getState());
        assertEquals(33432, host.getPostalCode());
        assertEquals(BigDecimal.valueOf(347), host.getStandardRate());
    }

    @Test
    void shouldNotFindByFakeHostId() {
        Host host = repository.findById("does not exist");

        assertNull(host);
    }

    @Test
    void shouldFindHostsLastName() {
        List<Host> hosts = repository.findByLastName("Whapple");

        assertNotEquals(0, hosts.size());
        assertEquals("Whapple", hosts.get(0).getLastName());
    }

    @Test
    void shouldNotFindHostsByNonExistingLastName() {
        List<Host> hosts = repository.findByLastName("Barritt");

        assertEquals(0, hosts.size());
    }

    @Test
    void shouldFindByEmail() {
        Host host = repository.findByEmail("pglenniec@feedburner.com");

        assertNotNull(host);
        assertEquals("Glennie", host.getLastName());
        assertEquals("0 Corscot Avenue", host.getAddress());
        assertEquals("343.75", host.getWeekendRate().toString());
    }
    @Test
    void shouldNotFindByExcludedEmail() {
        Host host = repository.findByEmail("pg13@amp.the");

        assertNull(host);
    }

    @Test
    void shouldFindHostsByState() {
        List<Host> hosts = repository.findByState("TX");

        assertEquals(7, hosts.size());

        assertEquals("Valasek", hosts.get(2).getLastName());
        assertEquals("McGrady", hosts.get(3).getLastName());
        assertEquals("Cubley", hosts.get(5).getLastName());
        assertEquals("Whapple", hosts.get(hosts.size() - 1).getLastName());
    }

    @Test
    void shouldNotFindByNonExistingState() {
        List<Host> hosts = repository.findByState("CA");

        assertEquals(0, hosts.size());

        hosts = repository.findByState("STATE");

        assertEquals(0, hosts.size());
    }

    @Test
    void shouldFindHostsByCity() {
        List<Host> hosts = repository.findByCity("San Antonio");

        assertEquals(2, hosts.size());

        assertEquals("McGrady", hosts.get(0).getLastName());
        assertEquals("7 Crowley Center", hosts.get(0).getAddress());

        assertEquals("Whapple", hosts.get(1).getLastName());
        assertEquals("(210) 4817817", hosts.get(1).getPhoneNumber());
    }

    @Test
    void shouldNotFindByNonExistingCity() {
        List<Host> hosts = repository.findByCity("");
        assertEquals(0, hosts.size());

        hosts = repository.findByCity("STATE");
        assertEquals(0, hosts.size());

        hosts = repository.findByCity("New York");
        assertEquals(0, hosts.size());
    }


}