package bnb.ui;

public enum MainMenuOption {

    EXIT(0, "Exit"),
    VIEW_RESERVATIONS(1, "View Reservations"),
    ADD_RESERVATION(2, "Add a Reservation"),
    EDIT_RESERVATION(3, "Edit a Reservation"),
    CANCEL_A_RESERVATION(4, "Cancel a Reservation");

    private final int value;
    private final String message;

    MainMenuOption(int optionValue, String optionMessage) {
        this.value = optionValue;
        this.message = optionMessage;
    }

    public static MainMenuOption fromValue(int value) {
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
