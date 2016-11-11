package com.repository;

import com.domain.Reservation;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Michał on 2016-11-07.
 */

public interface ReservationRepository extends PagingAndSortingRepository<Reservation, Long> {
}
