package bnb.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private int guestId;
    private BigDecimal cost;
    private String hostId;
    private Host host;
    private Guest guest;

    public Reservation() {

    }

    public Reservation(LocalDate startDate, LocalDate endDate, int guestId, String hostId, Host host, Guest guest) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestId = guestId;
        this.hostId = hostId;
        this.host = host;
        this.guest = guest;
    }

    public Reservation(int id, LocalDate startDate, LocalDate endDate, int guestId, BigDecimal cost, String hostId, Host host, Guest guest) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestId = guestId;
        this.cost = cost;
        this.hostId = hostId;
        this.host = host;
        this.guest = guest;
    }

    public int getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getGuestId() {
        return guestId;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getHostId() {
        return hostId;
    }

    public Host getHost() {
        return host;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "ID =" + id +
                ", Host =" + host +
                ", Guest =" + guest +
                ", Start Date =" + startDate +
                ", End Date =" + endDate +
                ", Guest ID =" + guestId +
                ", Cost =" + cost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id && guestId == that.guestId && Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate) && Objects.equals(cost, that.cost)
                && Objects.equals(hostId, that.hostId) && Objects.equals(host, that.host)
                && Objects.equals(guest, that.guest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, guestId, cost, hostId, host, guest);
    }
}
