package com.service;

import com.domain.Airport;
import com.repository.AirportRepository;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Michał on 2016-12-31.
 */
@Named
public class AirportServiceImpl implements AirportService {

    @Inject
    private AirportRepository airportRepository;

    @Override
    public Airport create(Airport airport) {
        return airportRepository.save(airport);
    }
}