package com.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Michał on 2016-11-12.
 */
@Entity
public class Ticket {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(unique = true)
    private Reservation reservation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}