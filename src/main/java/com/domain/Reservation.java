package com.domain;

import com.domain.listener.ReservationListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by Michał on 2016-11-07.
 */

@Entity
@EntityListeners(ReservationListener.class)
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Double price;

    @Valid
    @NotNull
    @ElementCollection
    private Set<Passenger> passengers;

    @Enumerated(EnumType.STRING)
    private Payment payment;

    @NotNull
    @OneToOne
    private Flight flight;

    @Column(unique = true)
    private String reservationCode;

    @Valid
    @NotNull
    @Embedded
    private Contact contact;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime reservationDate;

    @ManyToOne
    @JoinColumn
    @JsonBackReference
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @JsonProperty("active")
    @Transient
    public boolean getActive() {
        LocalDateTime date = Reservation.this.getFlight().getDepartureDate();
        LocalDateTime now = LocalDateTime.from(LocalDateTime.now());
        LocalDateTime departureMinusDay = LocalDateTime.from(date).minusDays(1L);
        return now.isBefore(departureMinusDay);
    }
}