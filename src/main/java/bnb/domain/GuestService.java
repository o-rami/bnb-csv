package bnb.domain;

import bnb.data.GuestRepository;
import bnb.models.Guest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public List<Guest> findAll() {
        return repository.findAll();
    }

    public Guest findById(int guestId) {
        return repository.findById(guestId);
    }

    public Guest findByEmail(String email) {
        return repository.findByEmail(email);
    }

}
