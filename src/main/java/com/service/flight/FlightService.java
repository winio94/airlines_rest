package com.service.flight;

import java.time.LocalDateTime;
import java.util.List;

import com.domain.Flight;

/**
 * Created by Michał on 2016-12-31.
 */
public interface FlightService {
    Flight create(Flight flight);

    List<Flight> findFlightsByCitiesAndDate(String from, String to, LocalDateTime departureDate);
}