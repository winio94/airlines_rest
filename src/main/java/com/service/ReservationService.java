package com.service;

import com.domain.CurrentUser;
import com.domain.Reservation;

import java.util.Set;

/**
 * Created by Michał on 2016-12-18.
 */
public interface ReservationService {
    Reservation findReservationByReservationCode(String reservationCode);

    boolean canDeletedReservation(CurrentUser currentUser, Long reservationId);

    Set<Reservation> findReservationsByCustomerId(Long id);

    void delete(Long id);
}