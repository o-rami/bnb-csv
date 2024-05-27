package bnb.data;

import bnb.models.Guest;
import bnb.models.Host;
import bnb.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository {

    private final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;

    public ReservationFileRepository(@Value("${reservationDirectoryPath}") String directory) {
        this.directory = directory;
    }

    @Override
    public List<Reservation> findByHostId(String hostId) {
        List<Reservation> hostsReservations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(hostId)))) {
            reader.readLine(); // skip header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    hostsReservations.add(deserialize(fields, hostId));
                }
            }
        } catch (FileNotFoundException ex) {
            //
        } catch (IOException ex) {
            //
        }

        return hostsReservations;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {

        if (reservation == null) {
            return null;
        }

        List<Reservation> all = findByHostId(reservation.getHostId());

        int nextId = all.stream()
                .mapToInt(Reservation::getId).max().orElse(0) + 1;
        reservation.setId(nextId);

        all.add(reservation);
        writeAllToFile(all, reservation.getHostId());

        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        if (reservation == null) {
            return false;
        }

        List<Reservation> all = findByHostId(reservation.getHostId());
        if (all == null) {
            return false;
        }

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == reservation.getId()) {
                all.get(i).setStartDate(reservation.getStartDate());
                all.get(i).setEndDate(reservation.getEndDate());
                writeAllToFile(all, reservation.getHostId());
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean delete(Reservation reservation) throws DataException {
        if (reservation == null) {
            return false;
        }

        List<Reservation> all = findByHostId(reservation.getHostId());
        if (all == null) {
            return false;
        }

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == reservation.getId()) {
                all.remove(i);
                writeAllToFile(all, reservation.getHostId());
                return true;
            }
        }

        return false;
    }

    private Reservation deserialize(String[] fields, String hostId) {
        // init
        Reservation reservation = new Reservation();

        // from fields
        reservation.setId(Integer.parseInt(fields[0]));
        reservation.setStartDate(LocalDate.parse(fields[1]));
        reservation.setEndDate(LocalDate.parse(fields[2]));
        reservation.setGuestId(Integer.parseInt(fields[3]));
        reservation.setCost(new BigDecimal(fields[4]));
        reservation.setHostId(hostId);

        Host host = new Host();
        host.setHostId(hostId);
        reservation.setHost(host);

        Guest guest = new Guest();
        guest.setGuestId(reservation.getGuestId());
        reservation.setGuest(guest);

        return reservation;
    }

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getId(),
                reservation.getStartDate().toString(),
                reservation.getEndDate().toString(),
                reservation.getGuestId(),
                reservation.getCost());
    }

    private void writeAllToFile(List<Reservation> reservationList, String hostId) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(hostId))) {
            writer.println(HEADER);

            for (Reservation r : reservationList) {
                writer.println(serialize(r));
            }
        } catch (IOException ex) {
            throw new DataException(ex);
        }
    }

    private String getFilePath(String hostId) {
        return Paths.get(directory, hostId + ".csv").toString();
    }

}
