package bnb.models;

import java.util.Objects;

public class Guest {

    private int guestId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String state;

    public Guest() {
    }

    public Guest(int guestId, String firstName, String lastName, String email, String phoneNumber, String state) {
        this.guestId = guestId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.state = state;
    }

    public int getGuestId() {
        return guestId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "Guest ID =" + guestId +
                ", First Name ='" + firstName + '\'' +
                ", Last Name ='" + lastName + '\'' +
                ", Email ='" + email + '\'' +
                ", Phone Number ='" + phoneNumber + '\'' +
                ", State ='" + state + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return guestId == guest.guestId && Objects.equals(firstName, guest.firstName)
                && Objects.equals(lastName, guest.lastName) && Objects.equals(email, guest.email)
                && Objects.equals(phoneNumber, guest.phoneNumber) && Objects.equals(state, guest.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guestId, firstName, lastName, email, phoneNumber, state);
    }
}
