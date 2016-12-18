package com.repository;

import com.domain.Reservation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * Created by Michał on 2016-11-07.
 */

public interface ReservationRepository extends PagingAndSortingRepository<Reservation, Long> {
    Reservation findReservationByReservationCode(@Param("reservationCode") String reservationCode);

    Set<Reservation> findReservationsByCustomerId(@Param("customerId") Long customerId);
}
