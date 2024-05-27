package bnb.domain;

import bnb.data.DataException;
import bnb.data.GuestRepositoryDouble;
import bnb.data.HostRepositoryDouble;
import bnb.data.ReservationRepositoryDouble;
import bnb.models.Guest;
import bnb.models.Host;
import bnb.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


class ReservationServiceTest {
    Guest guestA;
    Guest guestB;
    Host hostA;
    Host hostB;
    Reservation reservation;

    ReservationService service = new ReservationService(new ReservationRepositoryDouble(), new HostRepositoryDouble(),
            new GuestRepositoryDouble());


    @BeforeEach
    void setup() {
        guestA = new Guest(4, "Jesus", "Frias",
                "WakeUpSPD@Cap.com", "(917) 9876543", "NY");
        guestB = new Guest(7, "Homer", "Simpson",
                "FanOfTeDuff@sprfield.com", "(917) 6789678", "OH");
        hostA = new Host("cf6cd63a-028a-4620-9786-60e1d0ce23b7", "Vaisey",
                "cvaiseyn@ucsd.edu", "(208) 9563557", "71 Forest Dale Street",
                "Boise", "ID", 13732, new BigDecimal("150.00"), new BigDecimal("200.00"));
        hostB = new Host("5fee5bb6-0594-446b-bceb-bceb49241061", "Ramirez", "critFarming@sqen.org",
                "(123) 9876543", "28 Bale Avenue", "New York", "NY", 10022,
                new BigDecimal("100.00"), new BigDecimal("150.00"));
        reservation = new Reservation(2,
                LocalDate.of(2023, 5, 22),
                LocalDate.of(2023, 5, 24),
                guestA.getGuestId(), new BigDecimal("1150"), hostA.getHostId(), hostA, guestA);
    }

    @Test
    void shouldFindByHostId() {
        List<Reservation> all = service.findByHostId("cf6cd63a-028a-4620-9786-60e1d0ce23b7");

        assertEquals(1, all.size());
        assertEquals("Jesus", all.get(0).getGuest().getFirstName());
        assertEquals("NY", all.get(0).getGuest().getState());
    }

    @Test
    void shouldFindByLastName() {
        List<Reservation> byName = service.findByLastName("Vaisey");

        assertEquals(1, byName.size());
        assertEquals("Vaisey", byName.get(0).getHost().getLastName());
    }

    @Test
    void shouldNotFindFakeLastName() {
        List<Reservation> byName = service.findByLastName("Ramirez");
        assertEquals(0, byName.size());

        byName = service.findByLastName("");
        assertEquals(0, byName.size());

        byName = service.findByLastName(null);
        assertEquals(0, byName.size());
    }

    @Test
    void shouldFindByEmail() {
        List<Reservation> byHostEmail = service.findByHostEmail("cvaiseyn@ucsd.edu");

        assertEquals(1, byHostEmail.size());
        assertEquals("Vaisey", byHostEmail.get(0).getHost().getLastName());
    }

    @Test
    void shouldNotFindNonExistingEmail() {
        List<Reservation> byHostEmail = service.findByLastName("young@dumb.broke");
        assertEquals(0, byHostEmail.size());

        byHostEmail = service.findByLastName("");
        assertEquals(0, byHostEmail.size());

        byHostEmail = service.findByLastName(null);
        assertEquals(0, byHostEmail.size());
    }

    @Test
    void shouldFindByCity() {
        List<Reservation> byCity = service.findByCity("Boise");

        assertEquals(1, byCity.size());
        assertEquals("Vaisey", byCity.get(0).getHost().getLastName());
    }

    @Test
    void shouldNotFindByNonExistingCity() {
        List<Reservation> byCity = service.findByCity("Seattle");
        assertEquals(0, byCity.size());

        byCity = service.findByCity("");
        assertEquals(0, byCity.size());

        byCity = service.findByCity(null);
        assertEquals(0, byCity.size());
    }

    // BY state
    @Test
    void shouldFindByState() {
        List<Reservation> byState = service.findByState("ID");

        assertEquals(1, byState.size());
        assertEquals("Vaisey", byState.get(0).getHost().getLastName());
    }

    @Test
    void shouldNotFindNonExistingState() {
        List<Reservation> byState = service.findByState("");
        assertEquals(0, byState.size());

        byState = service.findByState("");
        assertEquals(0, byState.size());

        byState = service.findByState(null);
        assertEquals(0, byState.size());
    }


    @Test
    void shouldFindByZipCode() {
        List<Reservation> byPostalCode = service.findByPostalCode(13732);

        assertEquals(1, byPostalCode.size());
        assertEquals("Vaisey", byPostalCode.get(0).getHost().getLastName());
    }

    @Test
    void shouldNotFindByNonExistingZipCode() {
        List<Reservation> byPostalCode = service.findByPostalCode(11111);

        assertEquals(0, byPostalCode.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Result<Reservation> actual = service.add(reservation);

        assertTrue(actual.isSuccess());
        assert (actual.getErrorMessages().isEmpty());
    }

    @Test
    void shouldNotAddNull() throws DataException {
        Result<Reservation> actual = service.add(null);

        assertFalse(actual.isSuccess());
    }

    @Test
    void shouldNotAddWithoutNullStart() throws DataException {
        Reservation result = reservation;
        result.setStartDate(null);

        Result<Reservation> actual = service.add(result);
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getErrorMessages().size());
        assertEquals("Reservation start date is required.", actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotAddNullEnd() throws DataException {
        Reservation result = reservation;
        result.setEndDate(null);

        Result<Reservation> actual = service.add(result);

        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getErrorMessages().size());
        assertEquals("Reservation end date is required.", actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotAddNullHost() throws DataException {
        Reservation result = reservation;
        result.setHost(null);

        Result<Reservation> actual = service.add(result);

        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getErrorMessages().size());
        assertEquals("Host is required.", actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotAddNullHostId() throws DataException {
        Reservation result = reservation;
        result.setHostId(null);

        Result<Reservation> actual = service.add(result);

        assertFalse(actual.isSuccess());
        assertEquals("Host ID required.", actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotAddNullGuest() throws DataException {
        Reservation result = reservation;
        result.setGuest(null);

        Result<Reservation> actual = service.add(result);

        assertFalse(actual.isSuccess());
        assertEquals("Guest is required.", actual.getErrorMessages().get(0));

    }

    @Test
    void shouldNotAddInvalidGuestId() throws DataException {
        Reservation result = reservation;
        result.setGuestId(-1);

        Result<Reservation> actual = service.add(result);

        assertFalse(actual.isSuccess());
        assertEquals("Guest ID is invalid.", actual.getErrorMessages().get(0));

    }

    @Test
    void shouldNotAddEndBeforeStart() throws DataException {
        Reservation result = reservation;
        result.setStartDate(LocalDate.of(2023, 5, 26));

        Result<Reservation> actual = service.add(result);

        assertFalse(actual.isSuccess());
        assertEquals("Reservation start date must come before the end date.", actual.getErrorMessages().get(0));

    }

    @Test
    void shouldAddNonConflictingReservation() throws DataException {
        Reservation guestAReservation = reservation;
        Reservation guestBReservation = new Reservation(2,
                LocalDate.of(2023, 5, 22),
                LocalDate.of(2023, 5, 24),
                guestB.getGuestId(), new BigDecimal("1150"), hostA.getHostId(), hostA, guestB);

        Result<Reservation> actual = service.add(guestAReservation);
        assertTrue(actual.isSuccess());

        actual = service.add(guestBReservation);
        assertTrue(actual.isSuccess());
    }

    @Test
    void shouldNotAddConflictingReservation() throws DataException {
        Reservation guestAReservationTwo = new Reservation(2,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 24),
                guestA.getGuestId(), new BigDecimal("1150"), hostA.getHostId(), hostA, guestA);

        Result<Reservation> actual = service.add(guestAReservationTwo);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).contains("Reservation is overlapping"));
    }

    @Test
    void shouldNotAddNonExistingGuest() throws DataException {
        Guest fakeGuest = new Guest(5, "Marge", "Simpson",
                "BigBlueDoll@goo.gl", "(321) 2479247", "OH");
        Reservation result = reservation;
        result.setGuest(fakeGuest);
        result.setGuestId(fakeGuest.getGuestId());

        Result<Reservation> actual = service.add(result);

        assertFalse(actual.isSuccess());
        assertEquals("Guest does not exist.", actual.getErrorMessages().get(0));

    }

    @Test
    void shouldNotAddNonExistingHost() throws DataException {
        Host fakeHost = new Host("3a27cd11-b78f-4169-20e1-952d3f6525f2", "Scammer",
                "bcaa@notGNC.com", "(282) 0641064", "210 E 59th St", "New York", "NY",
                10031, new BigDecimal("20"), new BigDecimal("40"));
        Reservation result = reservation;
        result.setHost(fakeHost);
        result.setHostId(fakeHost.getHostId());

        Result<Reservation> actual = service.add(result);

        assertFalse(actual.isSuccess());
        assertEquals("Host does not exist.", actual.getErrorMessages().get(0));
    }

    @Test
    void shouldUpdate() throws DataException {
        Reservation update = reservation;
        update.setStartDate(reservation.getStartDate().plusDays(-1));

        Result<Reservation> actual = service.update(update);
        assertTrue(actual.isSuccess());
    }

    @Test
    void shouldNotUpdateInvalidChanges() throws DataException {
        Reservation update = reservation;
        update.setId(1);
        update.setGuest(guestB);
        update.setGuestId(guestB.getGuestId());

        update.setHost(hostB);

        Result<Reservation> actual = service.update(update);

        assertFalse(actual.isSuccess());
        assertEquals(2, actual.getErrorMessages().size());
        assertEquals("Guest cannot be changed.", actual.getErrorMessages().get(0));
        assertEquals("Host cannot be changed.", actual.getErrorMessages().get(1));
    }

    @Test
    void shouldDelete() throws DataException {
        Reservation toDelete = reservation;

        Result<Reservation> actual = service.delete(toDelete);

        assertTrue(actual.isSuccess());
    }

    @Test
    void shouldNotDeletePastReservation() throws DataException {
        Reservation toDelete = reservation;
        toDelete.setId(1);
        toDelete.setStartDate(toDelete.getStartDate().plusYears(-2));
        toDelete.setEndDate(toDelete.getEndDate().plusYears(-2));

        Result<Reservation> actual = service.delete(toDelete);

        assertFalse(actual.isSuccess());
        assertEquals("Past reservations cannot be cancelled.", actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotDeleteCurrentReservation() throws DataException {
        Reservation toDelete = reservation;
        toDelete.setId(1);
        toDelete.setStartDate(toDelete.getStartDate().plusYears(-2));

        Result<Reservation> actual = service.delete(toDelete);

        assertFalse(actual.isSuccess());
        assertEquals("Active reservations cannot be cancelled.", actual.getErrorMessages().get(0));
    }

    @Test
    void totalCostShouldBe300() throws DataException {
        Reservation result = reservation;
        result.setCost(BigDecimal.ZERO);

        Result<Reservation> actual = service.add(result);

        assertTrue(actual.isSuccess());
        assertEquals("300.00", result.getCost().toString()); // Enforces scale(2)
    }

    @Test
    void totalCostShouldBeZero() {
        Host newHost = hostA;
        hostA.setStandardRate(BigDecimal.ZERO);

        Reservation result = reservation;
        result.setHost(newHost);
        result.setCost(service.getTotalCost(result.getStartDate(), result.getEndDate(),
                result.getHost().getStandardRate(), result.getHost().getWeekendRate()));

        assertEquals(0, result.getCost().doubleValue()); // 0.00
    }

}