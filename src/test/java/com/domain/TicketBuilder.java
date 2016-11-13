package com.domain;

/**
 * Created by Michał on 2016-11-12.
 */
public final class TicketBuilder {
    private Reservation reservation;

    private TicketBuilder() {
    }

    public static TicketBuilder aTicket() {
        return new TicketBuilder();
    }

    public TicketBuilder withReservation(Reservation reservation) {
        this.reservation = reservation;
        return this;
    }

    public Ticket build() {
        Ticket ticket = new Ticket();
        ticket.setReservation(reservation);
        return ticket;
    }
}