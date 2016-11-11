package com.service;

import com.domain.Airport;
import com.domain.Customer;
import com.domain.Flight;
import com.domain.FlightClass;
import com.repository.AirportRepository;
import com.repository.CustomerRepository;
import com.repository.FlightRepository;
import com.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.*;

import static java.lang.String.valueOf;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Created by Michał on 2016-10-16.
 */

@Component
public class InitializationService {

    private static final String CITIES_FILE_PATH = "bigCities.txt";
    private static final Logger LOGGER = LoggerFactory.getLogger(InitializationService.class);
    public static final int MAX_PRICE = 1000;
    private final Random random = new Random();
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
        FlightClass[] flightClasses = FlightClass.values();
        flights.forEach(flight -> {
            flight.setFrom(airports.get(random.nextInt(airports.size())));
            flight.setTo(airports.get(random.nextInt(airports.size())));
            FlightClass flightClass = flightClasses[random.nextInt(flightClasses.length)];
            flight.setFlightClass(flightClass);
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
        Date departureDate = getDate(counter, today, cal);
        flight.setDepartureDate(departureDate);
        Date arrivalDate = getDate(counter, departureDate, cal);
        flight.setArrivalDate(arrivalDate);
        long between = MINUTES.between(localDateTime(departureDate), localDateTime(arrivalDate));
        flight.setDuration((int) between);
        double price = random.nextDouble() * MAX_PRICE;
        flight.setPrice(Math.round(price * 100d) / 100d);
        return flight;
    }

    private Temporal localDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private Date getDate(int counter, Date today, Calendar cal) {
        cal.setTime(today);
        cal.add(Calendar.MINUTE, counter);
        return cal.getTime();
    }

    private Customer customer(int counter) {
        Customer customer = new Customer();
        customer.setFirstName("firstName" + counter);
        customer.setLastName("lastName" + counter);
        return customer;
    }
}