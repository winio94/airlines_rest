package com.domain;

import java.util.Date;
import java.util.Set;

/**
 * Created by Michał on 2016-11-12.
 */
public final class ReservationBuilder {
    private Double price;
    private Set<Passenger> passengers;
    private Payment payment;
    private Flight flight;
    private String reservationCode;
    private Contact contact;
    private Date reservationDate;

    private ReservationBuilder() {
    }

    public static ReservationBuilder aReservation() {
        return new ReservationBuilder();
    }

    public ReservationBuilder withPrice(Double price) {
        this.price = price;
        return this;
    }

    public ReservationBuilder withPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
        return this;
    }

    public ReservationBuilder withPayment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public ReservationBuilder withFlight(Flight flight) {
        this.flight = flight;
        return this;
    }

    public ReservationBuilder withReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
        return this;
    }

    public ReservationBuilder withContact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public ReservationBuilder withReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
        return this;
    }

    public Reservation build() {
        Reservation reservation = new Reservation();
        reservation.setPrice(price);
        reservation.setPassengers(passengers);
        reservation.setPayment(payment);
        reservation.setFlight(flight);
        reservation.setReservationCode(reservationCode);
        reservation.setContact(contact);
        reservation.setReservationDate(reservationDate);
        return reservation;
    }
}