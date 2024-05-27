package bnb.data;

import bnb.models.Host;

import java.util.List;

public interface HostRepository {

    List<Host> findAll();

    Host findById(String GUID);

    List<Host> findByLastName(String lastName);

    Host findByEmail(String email);

    List<Host> findByState(String state);

    List<Host> findByCity(String city);

    List<Host> findByPostalCode(int zip);

}
