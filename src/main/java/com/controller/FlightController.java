package com.controller;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.service.flight.FlightService;
import com.util.date.DateTimeMilliSecondsFormat;

@RepositoryRestController
public class FlightController {

    @Inject
    private FlightService flightService;

    @GetMapping(value = "/flights/search/findFlights")
    public ResponseEntity<?> findFlights(@RequestParam("from") String from,
                                         @RequestParam("to") String to,
                                         @RequestParam("departureDate") @DateTimeMilliSecondsFormat("departureDate") LocalDateTime departureDate) {
        return ResponseEntity.ok(new Resources<>(flightService.findFlightsByCitiesAndDate(from, to, departureDate)));
    }
}