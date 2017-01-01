package com.service;

import com.domain.Flight;
import com.repository.FlightRepository;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Michał on 2016-12-31.
 */
@Named
public class FlightServiceImpl implements FlightService {

    @Inject
    private FlightRepository flightRepository;

    @Override
    public Flight create(Flight flight) {
        return flightRepository.save(flight);
    }
}