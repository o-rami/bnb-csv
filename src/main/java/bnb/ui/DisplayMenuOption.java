package bnb.ui;

public enum DisplayMenuOption {

    MAIN_MENU(0, "Go Back"),
    VIEW_BY_HOST_ID(1, "Host ID"),
    VIEW_BY_HOSTS_LAST_NAME(2, "Host's Last Name"),
    VIEW_BY_HOSTS_EMAIL(3, "Host's Email"),
    VIEW_BY_HOSTS_STATE(4, "Host's State"),
    VIEW_BY_HOST_CITY(5, "Host's City"),
    VIEW_BY_HOST_ZIP_CODE(6, "Host's Zip Code"),
    VIEW_BY_GUEST(7, "Guest's Email");
    private final int value;
    private final String message;

    DisplayMenuOption(int optionValue, String optionMessage) {
        this.value = optionValue;
        this.message = optionMessage;
    }

    public static DisplayMenuOption fromValue(int value) {
        for (DisplayMenuOption option : DisplayMenuOption.values()) {
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
