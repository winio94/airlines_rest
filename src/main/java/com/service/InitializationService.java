package com.service;

import com.domain.Airport;
import com.domain.Customer;
import com.domain.Flight;
import com.repository.AirportRepository;
import com.repository.CustomerRepository;
import com.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.String.valueOf;

/**
 * Created by Michał on 2016-10-16.
 */

@Component
public class InitializationService {

    public static final String CITIES_FILE_PATH = "src/main/resources/cities.txt";
    private final List<String> cities = FileUtil.readAllLinesFrom(CITIES_FILE_PATH, "\\s+");

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    AirportRepository airportRepository;

    public InitializationService() throws IOException {
    }

    @PostConstruct
    private void initialize() {
        for (int i = 0; i < cities.size(); i++) {
            airportRepository.save(airport(i));
            customerRepository.save(customer(i));
            flightRepository.save(flight(i));
        }
    }

    private Airport airport(int i) {
        Airport airport = new Airport();
        airport.setCity(cities.get(i));
        return airport;
    }

    private Flight flight(int counter) {
        Flight flight = new Flight();
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        flight.setFlightNumber(valueOf(counter));
        flight.setDepartureDate(getDate(counter, today, cal));
        return flight;
    }

    private Date getDate(int counter, Date today, Calendar cal) {
        cal.setTime(today);
        cal.add(Calendar.DATE, counter);
        return cal.getTime();
    }

    private Customer customer(int counter) {
        Customer customer = new Customer();
        customer.setFirstName("firstName" + counter);
        customer.setLastName("lastName" + counter);
        return customer;
    }
}