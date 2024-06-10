package bnb.ui;

import bnb.data.DataException;
import bnb.domain.GuestService;
import bnb.domain.HostService;
import bnb.domain.ReservationService;
import bnb.domain.Result;
import bnb.models.Guest;
import bnb.models.Host;
import bnb.models.Reservation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class Controller {

    private final GuestService guestService;
    private final HostService hostService;
    private final ReservationService reservationService;
    private final View view;

    public Controller(GuestService guestService, HostService hostService, ReservationService reservationService, View view) {
        this.guestService = guestService;
        this.hostService = hostService;
        this.reservationService = reservationService;
        this.view = view;
    }


    public void run() {
        view.displayHeader("Welcome to my BnB!");
        try {
            runApp();
        } catch (DataException ex) {
            view.displayErrors(List.of(ex.getMessage()));
        }
        view.displayMessage("Closing program. See ya next time!");
    }

    public void runApp() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS -> displayMenu();

                case ADD_RESERVATION -> addReservation();

                case EDIT_RESERVATION -> updateReservation();

                case CANCEL_A_RESERVATION -> cancelReservation();
            }
        } while (option != MainMenuOption.EXIT);
    }

    public void displayMenu() {
        DisplayMenuOption option;
        do {
            option = view.selectDisplayMenuOption();
            switch (option) {
                case MAIN_MENU -> {
                }

                case VIEW_BY_HOST_ID -> sortMenu();

                case VIEW_BY_HOSTS_LAST_NAME -> viewReservationByHostLastName();

                case VIEW_BY_HOSTS_EMAIL -> viewReservationByHostEmail();

                case VIEW_BY_HOSTS_STATE -> viewReservationByState();

                case VIEW_BY_HOST_CITY -> viewReservationByCity();

                case VIEW_BY_HOST_ZIP_CODE -> viewReservationByZipCode();

                case VIEW_BY_GUEST -> viewReservationByGuest();
            }
        } while (option != DisplayMenuOption.MAIN_MENU);
    }

    public void sortMenu() {
        SortMenuOption option;
        do {
            option = view.selectSortMenuOption();
            switch (option) {
                case MAIN_MENU -> {
                }

                case BY_RESERVATION_ID -> viewReservationByHostId(1);

                case BY_NEWEST -> viewReservationByHostId(2);

                case BY_OLDEST -> viewReservationByHostId(3);
            }
        } while (option != SortMenuOption.MAIN_MENU);
    }

    private void viewReservationByHostId(int num) {
        String hostId = view.getGUID("Enter host's ID: ");
        List<Reservation> reservations = reservationService.findByHostId(hostId);
        view.displayReservations(reservations, num);
        view.enterToContinue();
    }

    private void viewReservationByHostLastName() {
        String lastName = view.getString("Enter host's last name: ");
        List<Reservation> reservations = reservationService.findByLastName(lastName);
        view.displayReservationsByLastName(reservations);
        view.enterToContinue();
    }

    public void viewReservationByHostEmail() {
        String email = view.getEmail("Enter host's email: ");
        List<Reservation> reservations = reservationService.findByHostEmail(email);
        view.displayReservationsByEmail(reservations);
        view.enterToContinue();
    }

    private void viewReservationByState() {
        String state = view.getString("Enter host's state: ");
        List<Reservation> reservations = reservationService.findByState(state);
        view.displayReservationsByState(reservations);
        view.enterToContinue();
    }

    private void viewReservationByCity() {
        String city = view.getString("Enter host's city: ");
        List<Reservation> reservations = reservationService.findByCity(city);
        view.displayReservationsByCity(reservations);
        view.enterToContinue();
    }

    private void viewReservationByZipCode() {
        int zip = view.getInt("Enter host's postal code: ");
        List<Reservation> reservations = reservationService.findByPostalCode(zip);
        view.displayReservationsByZipCode(reservations);
        view.enterToContinue();
    }

    private void viewReservationByGuest() {
        String guestEmail = view.getEmail("Enter guest email: ");
        List<Host> hosts = hostService.findAll();
        List<Reservation> allReservations = new ArrayList<>();
        for (Host h : hosts) {
            allReservations.addAll(reservationService.findByHostId(h.getHostId()));
        }
        view.displayReservationByGuest(allReservations, guestEmail);
    }

    private void addReservation() throws DataException {
        view.displayHeader("Make a Reservation");

        Guest guest = getGuestViaEmail();
        if (guest == null) {
            view.displayMessage("Guest does not exist.");
            return;
        }
        Host host = getHostViaEmail();
        if (host == null) {
            view.displayMessage("Host does not exist.");
            return;
        }

        LocalDate start = view.getDate("Start (MM/dd/yyyy): ");
        LocalDate end = view.getDate("End (MM/dd/yyyy): ");

        Reservation toAdd = new Reservation(start, end, guest.getGuestId(), host.getHostId(), host, guest);
        toAdd.setCost(reservationService.getTotalCost(start, end, host.getStandardRate(), host.getWeekendRate()));

        if (view.getSummaryConfirmation(toAdd, toAdd.getCost())) {
            Result<Reservation> result = reservationService.add(toAdd);
            if (result.isSuccess()) {
                view.displayHeader("Success");
                view.displayMessage("Reservation " + result.getPayload().getId() + " created.");
            } else {
                view.displayErrors(result.getErrorMessages());
            }
        }
    }

    private void updateReservation() throws DataException {
        view.displayHeader("Edit a Reservation");

        Host host = getHostViaEmail();
        if (host == null) {
            view.displayMessage("Host does not exist.");
            return;
        }

        List<Reservation> reservationList = reservationService.findByHostId(host.getHostId());
        view.displayReservations(reservationList, 3);
        if (reservationList.size() == 0) {
            view.displayMessage("There are no reservations to update\n");
            return;
        }

        int reservationID = view.getInt("Enter the ID of the reservation you would like to change: ", 1, reservationList.size());
        Reservation toUpdate = findReservationById(reservationList, reservationID);


        String start = view.getOptionalDate("Start (" + toUpdate.getStartDate() + "): ");
        if (!start.equals("")) {
            toUpdate.setStartDate(LocalDate.parse(start));
        }
        String end = view.getOptionalDate("End (" + toUpdate.getEndDate() + "): ");
        if (!end.equals("")) {
            toUpdate.setEndDate(LocalDate.parse(end));
        }

        BigDecimal cost = reservationService.getTotalCost(toUpdate.getStartDate(), toUpdate.getEndDate(),
                host.getStandardRate(), host.getWeekendRate());

        if (view.getSummaryConfirmation(toUpdate, cost)) {
            Result<Reservation> result = reservationService.update(toUpdate);
            if (result.isSuccess()) {
                view.displayHeader("Success");
                view.displayMessage("Reservation " + result.getPayload().getId() + " updated.");
            } else {
                view.displayErrors(result.getErrorMessages());
            }
        }
    }

    private void cancelReservation() throws DataException {
        view.displayHeader("Cancel a Reservation");
        String hostEmail = view.getEmail("Host Email: ");
        Host host = hostService.findByEmail(hostEmail);
        if (host == null) {
            view.displayMessage("Host does not exist.");
            return;
        }

        List<Reservation> reservationList = reservationService.findByHostId(host.getHostId());
        List<Reservation> futureList = view.getFutureReservations(reservationList);

        if (futureList.size() == 0) {
            view.displayMessage("Host does not have any future reservations.");
        } else {
            view.displayReservations(futureList, 3);
            int toRemove = view.getInt("Enter the ID of the reservation you would like to cancel: ");
            Reservation toDelete = findReservationById(reservationList, toRemove);

            while (toDelete == null) {
                view.displayMessage("[INVALID ENTRY]");
                toRemove = view.getInt("Enter the ID of the reservation you would like to cancel: ");
                toDelete = findReservationById(reservationList, toRemove);
            }
            
            Result<Reservation> result = reservationService.delete(toDelete);
            if (result.isSuccess()) {
                view.displayHeader("Success");
                view.displayMessage("Reservation " + result.getPayload().getId() + " cancelled.");
            } else {
                view.displayErrors(result.getErrorMessages());
            }
        }
    }

    private Guest getGuestViaEmail() {
        String guestEmail = view.getEmail("Guest Email: ");
        return guestService.findByEmail(guestEmail);
    }


    private Host getHostViaEmail() {
        String hostEmail = view.getEmail("Host Email: ");
        return hostService.findByEmail(hostEmail);
    }

    private Reservation findReservationById(List<Reservation> reservations, int id) { //needed due to sorting
        for (Reservation r : reservations) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

}
