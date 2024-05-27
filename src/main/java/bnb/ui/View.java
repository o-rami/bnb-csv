package bnb.ui;

import bnb.models.Reservation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class View {

    private final TextIO io;

    public View(TextIO io) {
        this.io = io;
    }

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        for (MainMenuOption option : MainMenuOption.values()) {
            io.printf("[%s] %s%n", option.getValue(), option.getMessage());
        }
        String message = String.format("Select [%s-%s]: ", 0, MainMenuOption.values().length - 1);
        return MainMenuOption.fromValue(io.readInt(message, 0, MainMenuOption.values().length - 1));
    }

    public DisplayMenuOption selectDisplayMenuOption() {
        displayHeader("Display Menu");
        for (DisplayMenuOption option : DisplayMenuOption.values()) {
            io.printf("[%s] %s%n", option.getValue(), option.getMessage());
        }
        String message = String.format("Select search criteria [%s-%s]: ", 0, DisplayMenuOption.values().length - 1);
        return DisplayMenuOption.fromValue(io.readInt(message, 0, DisplayMenuOption.values().length - 1));
    }

    public SortMenuOption selectSortMenuOption() {
        displayHeader("Sort Menu");
        for (SortMenuOption option : SortMenuOption.values()) {
            io.printf("[%s] %s%n", option.getValue(), option.getMessage());
        }
        String message = String.format("Select sort criteria [%s-%s]: ", 0, SortMenuOption.values().length - 1);
        return SortMenuOption.fromValue(io.readInt(message, 0, SortMenuOption.values().length - 1));
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    public void displayHeader(String prompt) {
        io.println("\n||  " + prompt + "  ||");
        io.println("=".repeat(prompt.length() + 8));
    }

    public void displayMessage(String message) {
        io.println(message);
    }

    public void displayErrors(List<String> errors) {
        displayHeader("Errors:");
        for (String error : errors) {
            io.println(error);
        }
    }

    public void displayReservations(List<Reservation> reservations, int num) {
        if (reservations.size() == 0) {
            io.println("No reservations found.");
        } else {
            switch (num) {
                case 1 -> {
                } // default
                case 2 -> reservations.sort(Comparator.comparing(Reservation::getStartDate).reversed());
                case 3 -> reservations.sort(Comparator.comparing(Reservation::getStartDate));
            }
            io.printf("%n%s: %s, %s%n",
                    reservations.get(0).getHost().getLastName(),
                    reservations.get(0).getHost().getCity(),
                    reservations.get(0).getHost().getState());
            printReservations(reservations);
        }
        io.println("");
    }

    public void displayReservationsByLastName(List<Reservation> reservations) {
        reservations.sort(Comparator.comparing(Reservation::getStartDate));
        if (reservations.size() == 0) {
            io.println("\nNo reservations found.\n");
        } else {
            io.printf("%nBy last name, %s:%n", reservations.get(0).getHost().getLastName());
            printReservations(reservations);
        }
    }

    public void displayReservationsByEmail(List<Reservation> reservations) {
        reservations.sort(Comparator.comparing(Reservation::getStartDate));
        if (reservations.size() == 0) {
            io.println("\nNo reservations found.\n");
        } else {
            io.printf("%nBy email, %s:%n", reservations.get(0).getHost().getEmail());
            printReservations(reservations);
        }
    }

    public void displayReservationsByState(List<Reservation> reservations) {
        reservations.sort(Comparator.comparing(Reservation::getStartDate));
        if (reservations.size() == 0) {
            io.println("\nNo reservations found.\n");
        } else {
            io.printf("%nBy state, %s:%n", reservations.get(0).getHost().getState());
            printReservations(reservations);
        }
    }

    public void displayReservationsByCity(List<Reservation> reservations) {
        reservations.sort(Comparator.comparing(Reservation::getStartDate));
        if (reservations.size() == 0) {
            io.println("\nNo reservations found.\n");
        } else {
            io.printf("%nBy city, %s:%n", reservations.get(0).getHost().getCity());
            printReservations(reservations);
        }
    }

    public void displayReservationsByZipCode(List<Reservation> reservations) {
        reservations.sort(Comparator.comparing(Reservation::getStartDate));
        if (reservations.size() == 0) {
            io.println("\nNo reservations found.\n");
        } else {
            io.printf("%nBy postal code, %s:%n", reservations.get(0).getHost().getPostalCode());
            printReservations(reservations);
        }
    }

    public void displayReservationByGuest(List<Reservation> reservations, String guestEmail) {
        reservations = reservations.stream()
                .filter(r -> r.getGuest().getEmail().equalsIgnoreCase(guestEmail)).collect(Collectors.toList());
        if (reservations.size() == 0) {
            io.println("\nNo reservations found.\n");
        } else {

            io.printf("%nBy guest, %s:%n", reservations.get(0).getGuest().getFirstName() +
                    " " + reservations.get(0).getGuest().getLastName());
            io.printf("%-3s | %10s - %-10s | %-18s | %s%n",
                    "ID#",
                    "Start Date",
                    "End Date",
                    "Host Address",
                    "Host's Email");
            for (Reservation r : reservations) {
                io.printf("%-3s | %10s - %10s | %-18s | %s%n",
                        r.getId(),
                        r.getStartDate().toString(),
                        r.getEndDate().toString(),
                        r.getHost().getAddress(),
                        r.getHost().getEmail());
            }
        }
    }


    public List<Reservation> getFutureReservations(final List<Reservation> reservations) { // self check
        List<Reservation> futureReservations = new ArrayList<>(reservations);
        futureReservations.removeIf(r -> r.getStartDate().isBefore(LocalDate.now()));
        return futureReservations;
    }

    public int getInt(String prompt) {
        return io.readInt(prompt);
    }

    public String getString(String prompt) {
        return io.readRequiredString(prompt);
    }

    public String getGUID(String prompt) {
        return io.readGUID(prompt);
    }

    public String getEmail(String prompt) {
        return io.readEmail(prompt);
    }

    public String getOptionalDate(String prompt) {
        while (true) {
            String input = io.readString(prompt);
            if (input.equals("")) {
                return input;
            } else {
                try {
                    return LocalDate.parse(input, io.getFormatter()).toString();
                } catch (DateTimeParseException ex) {
                    io.println(io.getInvalidDate());
                }
            }
        }
    }

    public LocalDate getDate(String prompt) {
        return io.readLocalDate(prompt);
    }

    public boolean getSummaryConfirmation(Reservation reservation, BigDecimal cost) {
        io.printf("Start: %s%n", reservation.getStartDate().toString());
        io.printf("End: %s%n", reservation.getEndDate().toString());
        io.printf("Total: %s%n", cost.toString());
        return io.readBoolean("It this correct? [y/n]: ");
    }

    public int getInt(String prompt, int min, int max) {
        return io.readInt(prompt, min, max);
    }

    private void printReservations(List<Reservation> reservations) {
        io.printf("%-3s | %10s - %-10s | %-22s | %s%n",
                "ID#",
                "Start Date",
                "End Date",
                "Guest's Name",
                "Guest's Email");
        for (Reservation r : reservations) {
            io.printf("%-3s | %10s - %10s | %-22s | %s%n",
                    r.getId(),
                    r.getStartDate().toString(),
                    r.getEndDate().toString(),
                    r.getGuest().getLastName() + ", " + r.getGuest().getFirstName(),
                    r.getGuest().getEmail());
        }
    }
}
