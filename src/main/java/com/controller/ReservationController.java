package com.controller;

import com.domain.Reservation;
import com.repository.ReservationRepository;
import com.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Objects;

/**
 * Created by Michał on 2016-11-12.
 */
@RestController
public class ReservationController {

    @Inject
    private TicketService ticketService;

    @Inject
    private ReservationRepository reservationRepository;

    @DeleteMapping(value = "/reservations/{id}")
    public ResponseEntity<String> removeReservation(@PathVariable Long id, @RequestBody String reservationCode) {
        Reservation reservation = reservationRepository.findReservationByReservationCode(reservationCode);
        if (Objects.nonNull(reservation) && reservation.getId().equals(id)) {
            reservationRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}