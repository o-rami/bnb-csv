package bnb.data;

import bnb.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {

    private ArrayList<Guest> guests = new ArrayList<>();

    public GuestRepositoryDouble() {
        guests.add(new Guest(4,"Jesus","Frias","WakeUpSPD@Cap.com","(917) 9876543","NY"));
        guests.add(new Guest(7, "Homer", "Simpson","FanOfTeDuff@sprfield.com", "(917) 6789678", "OH"));
        guests.add(new Guest(2,"Nye","Ritter","nrittere@dagondesign.com","(609) 7576171","NJ"));
        guests.add(new Guest(3,"Jacquenetta","Judgkins","jjudgkinsh@goo.gl","(802) 9364252","VT"));
    }

    @Override
    public List<Guest> findAll() {
        return guests;
    }

    @Override
    public Guest findById(int id) {
        for (Guest g : guests) {
            if (g.getGuestId() == id) {
                return g;
            }
        }
        return null;
    }

    @Override
    public Guest findByEmail(String email) {
        for (Guest g : guests) {
            if (g.getEmail().equalsIgnoreCase(email)) {
                return g;
            }
        }
        return null;
    }

    @Override
    public Guest findByState(String state) {
        for (Guest g : guests) {
            if (g.getState().equalsIgnoreCase(state)) {
                return g;
            }
        }
        return null;
    }

}
