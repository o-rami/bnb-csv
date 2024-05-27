package bnb.data;

import bnb.models.Guest;
import bnb.models.Host;

import java.util.List;

public interface GuestRepository {

    List<Guest> findAll();

    Guest findById(int id);

    public Guest findByEmail(String email);

    public Guest findByState(String state);

}
