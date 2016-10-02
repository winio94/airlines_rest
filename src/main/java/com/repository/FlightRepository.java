package com.repository;

import com.domain.Flight;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Michał on 2016-10-02.
 */
public interface FlightRepository extends PagingAndSortingRepository<Flight, Long> {

    Flight findByFlightNumber(@Param("number") String number);
}
