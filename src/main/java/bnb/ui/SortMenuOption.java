package bnb.ui;

public enum SortMenuOption {

    MAIN_MENU(0, "Go Back"),
    BY_RESERVATION_ID(1, "Sort By ID"),
    BY_NEWEST(2, "Sort by Newest"),
    BY_OLDEST(3, "Sort by Oldest");

    private final int value;
    private final String message;

    SortMenuOption(int optionValue, String optionMessage) {
        this.value = optionValue;
        this.message = optionMessage;
    }

    public static SortMenuOption fromValue(int value) {
        for (SortMenuOption option : SortMenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return MAIN_MENU;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
