package bnb.domain;

import bnb.data.HostRepository;
import bnb.models.Host;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostService {

    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public List<Host> findAll() {
        return repository.findAll();
    }

    public Host findById(String GUID) {
        return repository.findById(GUID);
    }

    public List<Host> findByLastName(String lastName) {
        return repository.findByLastName(lastName);
    }

    public Host findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<Host> findByState(String state) {
        return repository.findByState(state);
    }

}
