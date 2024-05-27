package bnb.data;

import bnb.models.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestFileRepository implements GuestRepository {
    // private final String HEADER = "guest_id,first_name,last_name,email,phone,state";
    private final String filePath;

    public GuestFileRepository(@Value("${guestFilePath}") String filePath) {
        this.filePath = filePath;
    }

    //FIND
    @Override
    public List<Guest> findAll() {
        ArrayList<Guest> guestList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    guestList.add(deserialize(fields));
                }
            }

        } catch (FileNotFoundException ex) {
            //
        } catch (IOException ex) {
            // don't throw on read
        }

        return guestList;
    }

    @Override
    public Guest findById(int id) {
        List<Guest> all = findAll();
        for (Guest guest : all) {
            if (guest.getGuestId() == id) {
                return guest;
            }
        }
        return null;
    }

    @Override
    public Guest findByEmail(String email) {
        List<Guest> all = findAll();
        for (Guest guest : all) {
            if (guest.getEmail().equalsIgnoreCase(email)) {
                return guest;
            }
        }
        return null;
    }

    @Override
    public Guest findByState(String state) {
        List<Guest> all = findAll();
        for (Guest guest : all) {
            if (guest.getState().equalsIgnoreCase(state)) {
                return guest;
            }
        }
        return null;
    }

    private Guest deserialize(String[] fields) {
        Guest newGuest = new Guest();
        newGuest.setGuestId(Integer.parseInt(fields[0]));
        newGuest.setFirstName(fields[1]);
        newGuest.setLastName(fields[2]);
        newGuest.setEmail(fields[3]);
        newGuest.setPhoneNumber(fields[4]);
        newGuest.setState(fields[5]);
        return newGuest;
    }

}
