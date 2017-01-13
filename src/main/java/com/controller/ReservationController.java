package com.controller;

import com.domain.Reservation;
import com.domain.Ticket;
import com.service.ReservationService;
import com.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.security.Principal;
import java.util.Objects;

/**
 * Created by Michał on 2016-11-12.
 */
@RestController
public class ReservationController {

    @Inject
    private ReservationService reservationService;

    @Inject
    private TicketService ticketService;

    @PreAuthorize("@reservationServiceImpl.canDeletedReservation(principal, #id)")
    @DeleteMapping(value = "/reservations/{id}")
    public ResponseEntity<String> removeReservation(Principal principal, @PathVariable Long id, @RequestBody String reservationCode) {
        Reservation reservation = reservationService.findReservationByReservationCode(reservationCode);
        if (Objects.nonNull(reservation) && reservation.getId().equals(id)) {
            Ticket ticketByReservationId = ticketService.findTicketByReservationId(reservation.getId());
            ticketService.delete(ticketByReservationId);
            reservationService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}