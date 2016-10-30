package com.service;

import com.domain.Airport;
import com.domain.Customer;
import com.domain.FLightClass;
import com.domain.Flight;
import com.repository.AirportRepository;
import com.repository.CustomerRepository;
import com.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

import static java.lang.String.valueOf;

/**
 * Created by Michał on 2016-10-16.
 */

@Component
public class InitializationService {

    private static final String CITIES_FILE_PATH = "bigCities.txt";
    private static final Logger LOGGER = LoggerFactory.getLogger(InitializationService.class);
    private List<String> cities = new ArrayList<>();
    private List<Flight> flights = new ArrayList<>();
    private List<Airport> airports = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    public InitializationService() throws IOException {
    }

    @PostConstruct
    private void initialize() {
        initializeCities();
        initializeAirports();
        initializeCustomers();
        initializeFlights();
    }

    private void initializeCities() {
        cities = FileUtil.readAllLinesFrom(CITIES_FILE_PATH, "\\s+");
        LOGGER.debug("INITIALIZE CITIES. Cities size : {}.", cities.size());
    }

    private void initializeAirports() {
        for (int i = 0; i < cities.size(); i++) {
            Airport airport = airport(i);
            airports.add(airport);
            airportRepository.save(airport);
        }
    }

    private void initializeCustomers() {
        for (int i = 0; i < 500; i++) {
            Customer customer = customer(i);
            customers.add(customer);
            customerRepository.save(customer);
            flights.add(flight(i));
        }
    }

    private void initializeFlights() {
        Random random = new Random();
        FLightClass[] fLightClasses = FLightClass.values();
        flights.forEach(flight -> {
            flight.setFrom(airports.get(random.nextInt(airports.size())));
            flight.setTo(airports.get(random.nextInt(airports.size())));
            FLightClass fLightClass = fLightClasses[random.nextInt(fLightClasses.length)];
            flight.setfLightClass(fLightClass);
            flightRepository.save(flight);
        });
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