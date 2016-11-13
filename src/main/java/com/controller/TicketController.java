package com.controller;

import com.domain.Reservation;
import com.domain.Ticket;
import com.repository.ReservationRepository;
import com.service.TicketService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by Michał on 2016-11-12.
 */
@RestController
public class TicketController {

    @Inject
    private TicketService ticketService;

    @Inject
    private ReservationRepository reservationRepository;

    @Transactional
    @PostMapping(value = "/tickets", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Ticket createTicketFor(@RequestBody String reservationCode) {
        Reservation reservation = reservationRepository.findReservationByReservationCode(reservationCode);
        Ticket ticket = new Ticket();
        ticket.setReservation(reservation);
        return ticketService.save(ticket);
    }
}