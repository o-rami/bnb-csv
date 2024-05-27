package bnb.data;

import bnb.models.Reservation;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findByHostId(String hostId);

    Reservation add(Reservation reservation) throws DataException;

    boolean update(Reservation reservation) throws DataException;

    boolean delete(Reservation reservation) throws DataException;

}
