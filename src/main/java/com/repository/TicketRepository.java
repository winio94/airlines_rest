package com.repository;

import com.domain.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Michał on 2016-11-12.
 */
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Ticket findTicketByReservationId(@Param("reservationId") Long reservationId);
}