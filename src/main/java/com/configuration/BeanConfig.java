package com.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.repository.AirportRepository;
import com.service.flight.FlightRepository;
import com.service.flight.FlightService;
import com.service.flight.FlightServiceImpl;
import com.service.shortest_path.DistanceCalculator;

@Configuration
public class BeanConfig {

    @Bean
    public FlightService FlightService(FlightRepository flightRepository, AirportRepository airportReporitory) {
        return new FlightServiceImpl(flightRepository, new DistanceCalculator(), airportReporitory);
    }
}
