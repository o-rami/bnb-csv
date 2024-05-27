package bnb.data;

import bnb.domain.ReservationService;
import bnb.models.Guest;
import bnb.models.Host;
import bnb.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReservationRepositoryDouble implements ReservationRepository {
    private final Host host = new Host("cf6cd63a-028a-4620-9786-60e1d0ce23b7", "Vaisey",
            "cvaiseyn@ucsd.edu", "(208) 9563557", "71 Forest Dale Street", "Boise",
            "ID", 13732, BigDecimal.valueOf(150), BigDecimal.valueOf(200));

    private final Guest guest = new Guest(4, "Jesus", "Frias",
            "WakeUpSPD@Cap.com", "(917) 9876543", "NY");

    private final Guest guestTwo = new Guest(8, "Victoria", "Munez",
            "EorzeanTato@sqen.com", "(917) 3213213", "NC");
    private final Reservation first = new Reservation(1,
            LocalDate.of(2024, 5, 14),
            LocalDate.of(2024, 5, 20),
            guest.getGuestId(), new BigDecimal("1150"), host.getHostId(), host, guest);

    private final ArrayList<Reservation> reservations = new ArrayList<>();

    public ReservationRepositoryDouble() {
        reservations.add(first);
    }

    @Override
    public List<Reservation> findByHostId(String hostId) {
        if (reservations.get(0).getHostId() == hostId) {
            return new ArrayList<>(reservations);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        reservation.setId(55);
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        return reservation.getId() > 0;
    }

    @Override
    public boolean delete(Reservation reservation) throws DataException {
        return reservation.getId() > 0;
    }

}
