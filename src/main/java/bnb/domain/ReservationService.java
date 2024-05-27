package bnb.domain;

import bnb.data.DataException;
import bnb.data.GuestRepository;
import bnb.data.HostRepository;
import bnb.data.ReservationRepository;
import bnb.models.Guest;
import bnb.models.Host;
import bnb.models.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final GuestRepository guestRepository;
    private final HostRepository hostRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository, HostRepository hostRepository,
                              GuestRepository guestRepository) {

        this.reservationRepository = reservationRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
    }

    public List<Reservation> findByHostId(String hostId) {
        List<Reservation> result = reservationRepository.findByHostId(hostId);
        fillFields(result);

        return result;
    }

    public List<Reservation> findByLastName(String lastName) {
        List<Host> hosts = hostRepository.findByLastName(lastName);

        List<Reservation> result = compileLists(hosts);
        fillFields(result);

        return result;
    }

    public List<Reservation> findByHostEmail(String email) {
        Host host = hostRepository.findByEmail(email);
        if (host == null) {
            System.out.println("\nHost does not exist.");
            return new ArrayList<>();
        }
        List<Reservation> result = reservationRepository.findByHostId(host.getHostId());
        fillFields(result);

        return result;
    }

    public List<Reservation> findByState(String state) {
        List<Host> hosts = hostRepository.findByState(state);
        List<Reservation> result = compileLists(hosts);
        fillFields(result);

        return result;
    }

    public List<Reservation> findByCity(String city) {
        List<Host> hosts = hostRepository.findByCity(city);
        List<Reservation> result = compileLists(hosts);
        fillFields(result);

        return result;
    }

    public List<Reservation> findByPostalCode(int zip) {
        List<Host> hosts = hostRepository.findByPostalCode(zip);
        List<Reservation> result = compileLists(hosts);
        fillFields(result);

        return result;
    }

    public Result<Reservation> add(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        validatePeriod(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        if (reservation.getStartDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("New reservations start dates must not have passed.");
            return result;
        }

        reservation.setCost(getTotalCost(reservation.getStartDate(), reservation.getEndDate(),
                reservation.getHost().getStandardRate(), reservation.getHost().getWeekendRate()));
        result.setPayload(reservationRepository.add(reservation));

        return result;
    }

    public Result<Reservation> update(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        validatePeriod(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }
        validateEdit(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        reservation.setCost(getTotalCost(reservation.getStartDate(), reservation.getEndDate(),
                reservation.getHost().getStandardRate(), reservation.getHost().getWeekendRate()));

        result.setPayload(reservation);
        boolean updated = reservationRepository.update(reservation);

        if (!updated) {
            result.addErrorMessage("Reservation cannot be updated because it does not exist.");
        }

        result.setPayload(reservation);

        return result;
    }

    public Result<Reservation> delete(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        validateCancellation(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        if (result.isSuccess()) {
            boolean deleted = reservationRepository.delete(reservation);

            if (!deleted) {
                result.addErrorMessage("Reservation cannot be deleted because it does not exist.");
            }
        }

        result.setPayload(reservation);

        return result;
    }

    public BigDecimal getTotalCost(LocalDate startDate, LocalDate endDate,
                                   BigDecimal standardRate, BigDecimal weekendRate) {
        LocalDate start = startDate;
        BigDecimal total = BigDecimal.ZERO;

        while (start.isBefore(endDate)) {
            if (start.getDayOfWeek() == DayOfWeek.FRIDAY || start.getDayOfWeek() == DayOfWeek.SATURDAY) {
                total = total.add(weekendRate);
            } else {
                total = total.add(standardRate);
            }
            start = start.plusDays(1);
        }
        return total.setScale(2, RoundingMode.HALF_EVEN);
    }

    private void fillFields(List<Reservation> reservations) {
        Map<String, Host> hostMap = hostRepository.findAll().stream()
                .collect(Collectors.toMap(Host::getHostId, h -> h));
        Map<Integer, Guest> guestMap = guestRepository.findAll().stream()
                .collect(Collectors.toMap(Guest::getGuestId, g -> g));

        for (Reservation r : reservations) {
            r.setHost(hostMap.get(r.getHost().getHostId()));
            r.setGuest(guestMap.get(r.getGuest().getGuestId()));
            r.setCost(getTotalCost(r.getStartDate(), r.getEndDate(),
                    r.getHost().getStandardRate(), r.getHost().getWeekendRate()));
        }
    }

    private List<Reservation> compileLists(List<Host> hosts) {
        List<Reservation> result = new ArrayList<>();
        if (hosts.size() > 0) {
            for (Host h : hosts) {
                result.addAll(reservationRepository.findByHostId(h.getHostId()));
            }
        }
        return result;
    }

    private Result<Reservation> validate(Reservation reservation) {
        Result<Reservation> result = validateNulls(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        validateFields(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validatePersons(reservation, result);

        return result;
    }

    private Result<Reservation> validateNulls(Reservation reservation) {
        Result<Reservation> result = new Result<>();
        if (reservation == null) {
            result.addErrorMessage("Reservation cannot be null.");
            return result;
        }
        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Reservation start date is required.");
        }
        if (reservation.getEndDate() == null) {
            result.addErrorMessage("Reservation end date is required.");
        }
        if (reservation.getHost() == null) {
            result.addErrorMessage("Host is required.");
        }
        if (reservation.getHostId() == null) {
            result.addErrorMessage("Host ID required.");
        }
        if (reservation.getGuest() == null) {
            result.addErrorMessage("Guest is required.");
        }
        return result;
    }

    private void validateFields(Reservation reservation, Result<Reservation> result) {
        if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            result.addErrorMessage("Reservation start date must come before the end date.");
        }

        if (reservation.getGuestId() <= 0) {
            result.addErrorMessage("Guest ID is invalid.");
        }

        if (reservation.getHostId().isBlank() || reservation.getHostId().isEmpty()) {
            result.addErrorMessage("Host ID is invalid.");
        }

    }

    private void validatePeriod(Reservation reservation, Result<Reservation> result) {
        List<Reservation> all = reservationRepository.findByHostId(reservation.getHostId());
        if (all.size() == 0) {
            return;
        }
        for (Reservation r : all) {
            if (r.getId() != reservation.getId()) {
                boolean isBefore = r.getEndDate().compareTo(reservation.getStartDate()) <= 0;
                boolean isAfter = r.getStartDate().compareTo(reservation.getEndDate()) >= 0;

                if (!isBefore && !isAfter) {
                    String message = String.format("Reservation is overlapping the following reservation:" +
                                    "%nHost: %s | Guest: %s %s | %s - %s",
                            r.getHost().getLastName(),
                            r.getGuest().getFirstName(),
                            r.getGuest().getLastName(),
                            r.getStartDate().toString(),
                            r.getEndDate().toString());

                    result.addErrorMessage(message);
                }
            }
        }
    }

    private void validatePersons(Reservation reservation, Result<Reservation> result) {
        if (reservation.getHost() == null || hostRepository.findById(reservation.getHostId()) == null) {
            result.addErrorMessage("Host does not exist.");
        }

        if (guestRepository.findById(reservation.getGuestId()) == null) {
            result.addErrorMessage("Guest does not exist.");
        }
    }

    private void validateEdit(Reservation reservation, Result<Reservation> result) {
        List<Reservation> reservations = reservationRepository.findByHostId(reservation.getHostId());
        for (Reservation r : reservations) {
            if (r.getId() == reservation.getId()) {
                if (r.getGuestId() != reservation.getGuestId() ||
                        !guestRepository.findById(r.getGuestId()).getLastName()
                                .equalsIgnoreCase(reservation.getGuest().getLastName())) {
                    result.addErrorMessage("Guest cannot be changed.");
                }
                if (!r.getHostId().equals(reservation.getHostId()) ||
                        !hostRepository.findById(r.getHostId()).getLastName()
                                .equals(reservation.getHost().getLastName())) {
                    result.addErrorMessage("Host cannot be changed.");
                }
            }
        }
    }

    private void validateCancellation(Reservation reservation, Result<Reservation> result) {
        if (reservation.getEndDate().isBefore(LocalDate.now())
                && reservation.getStartDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Past reservations cannot be cancelled.");
        } else if (reservation.getStartDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Active reservations cannot be cancelled.");
        }
    }

}
