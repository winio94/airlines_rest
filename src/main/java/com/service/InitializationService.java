package com.service;

import com.domain.*;
import com.repository.AirportRepository;
import com.repository.CustomerRepository;
import com.repository.FlightRepository;
import com.repository.UserRepository;
import com.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.String.valueOf;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Created by Michał on 2016-10-16.
 */

@Named
public class InitializationService {

    private static final int MAX_PRICE = 1000;
    private static final int CUSTOMER_AMOUNT = 10;
    private static final int FLIGHT_AMOUNT = 1000;
    private static final String CITIES_FILE_PATH = "bigCities.txt";
    private static final String PASSWORD = "password";
    private static final String EMAIL_PREFIX = "email";
    private static final String EMAIL_SUFFIX = "@gmail.com";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final Logger LOGGER = LoggerFactory.getLogger(InitializationService.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final Random random = new Random();
    private List<String> cities = new ArrayList<>();
    private List<Airport> airports = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private FlightRepository flightRepository;

    @Inject
    private AirportRepository airportRepository;

    @Inject
    private UserRepository userRepository;

    public InitializationService() throws IOException {
    }

    @PostConstruct
    private void initialize() {
        initializeAdmin();
        initializeCities();
        initializeAirports();
        initializeCustomers();
        initializeFlights();
    }

    private void initializeAdmin() {
        userRepository.save(admin());
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
        for (int i = 0; i < CUSTOMER_AMOUNT; i++) {
            Customer customer = customer(i);
            customers.add(customer);
            customerRepository.save(customer);
        }
    }

    private void initializeFlights() {
        FlightClass[] flightClasses = FlightClass.values();
        for (int i = 0; i < FLIGHT_AMOUNT; i++) {
            Flight flight = flight(i);
            flight.setFrom(airports.get(random.nextInt(airports.size())));
            flight.setTo(airports.get(random.nextInt(airports.size())));
            FlightClass flightClass = flightClasses[random.nextInt(flightClasses.length)];
            flight.setFlightClass(flightClass);
            flightRepository.save(flight);
        }
    }

    private User user(String email, String userpass) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(userpass));
        user.setRole(Role.USER);
        return user;
    }

    private User admin() {
        User admin = new User();
        admin.setEmail("admin@admin.com");
        admin.setPassword(encoder.encode("adminpass"));
        admin.setRole(Role.ADMIN);
        return admin;
    }

    private Airport airport(int i) {
        Airport airport = new Airport();
        airport.setCity(cities.get(i));
        return airport;
    }

    private Flight flight(int counter) {
        Flight flight = new Flight();
        LocalDateTime today = LocalDateTime.now();
        flight.setFlightNumber(valueOf(counter));
        LocalDateTime departureDate = getDate(counter, today);
        flight.setDepartureDate(departureDate);
        LocalDateTime arrivalDate = getDate(counter, departureDate);
        flight.setArrivalDate(arrivalDate);
        long between = MINUTES.between(departureDate, arrivalDate);
        flight.setDuration((int) between);
        double price = random.nextDouble() * MAX_PRICE;
        flight.setPrice(Math.round(price * 100d) / 100d);
        return flight;
    }

    private LocalDateTime getDate(int counter, LocalDateTime today) {
        return LocalDateTime.from(today).plusMinutes(counter);
    }

    private Customer customer(int counter) {
        Customer customer = new Customer();
        customer.setFirstName(FIRST_NAME + counter);
        customer.setLastName(LAST_NAME + counter);
        User user = user(getEmail(counter), getUserPassword(counter));
        customer.setUser(user);
        return customer;
    }

    private String getEmail(int counter) {
        return EMAIL_PREFIX + counter + EMAIL_SUFFIX;
    }

    private String getUserPassword(int counter) {
        return PASSWORD + counter;
    }
}