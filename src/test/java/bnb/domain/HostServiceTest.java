package bnb.domain;

import bnb.data.GuestRepositoryDouble;
import bnb.data.HostRepository;
import bnb.data.HostRepositoryDouble;
import bnb.models.Host;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {

    private final HostRepository repository = new HostRepositoryDouble();

    @Test
    void shouldFindAll() {
        List<Host> hosts = repository.findAll();

        assertEquals(3, hosts.size());
        assertEquals("3a27cd11-b78f-4538-80c1-952d3a6525f2", hosts.get(1).getHostId());
        assertEquals("(208) 9563557", hosts.get(2).getPhoneNumber());
    }

    @Test
    void shouldFindById() {
        Host host = repository.findById("3a27cd11-b78f-4538-80c1-952d3a6525f2");

        assertNotNull(host);
        assertEquals("123 W 55th St", host.getAddress());
    }

    @Test
    void shouldNotFindNull() {
        Host badHost = repository.findById(null);

        assertNull(badHost);
    }

    @Test
    void shouldNotFindNonExisting() {
        Host badHost = repository.findById("5");

        assertNull(badHost);
    }
}