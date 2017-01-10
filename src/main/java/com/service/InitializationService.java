package com.service;

import com.domain.*;
import com.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final Random random = new Random();
    private List<String> cities = new ArrayList<>();
    private List<Airport> airports = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();

    @Inject
    private CustomerService customerService;

    @Inject
    private FlightService flightService;

    @Inject
    private AirportService airportService;

    @Inject
    private UserService userService;

//    @Inject
//    private BCryptPasswordEncoder encoder;

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
        User admin = admin();
        Customer adminCustomer = new Customer();
        adminCustomer.setUser(admin);
        customerService.create(adminCustomer);
    }

    private void initializeCities() {
        cities = FileUtil.readAllLinesFrom(CITIES_FILE_PATH, "\\s+");
        LOGGER.debug("INITIALIZE CITIES. Cities size : {}.", cities.size());
    }

    private void initializeAirports() {
        for (int i = 0; i < cities.size(); i++) {
            Airport airport = airport(i);
            airports.add(airport);
            airportService.create(airport);
        }
    }

    private void initializeCustomers() {
        for (int i = 0; i < CUSTOMER_AMOUNT; i++) {
            customerService.create(customer(i));
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
            flightService.create(flight);
        }
    }

    private User admin() {
        User admin = new User();
        admin.setEmail("admin@admin.com");
        admin.setPassword("adminpass");
        admin.setRole(Role.ADMIN);
        return admin;
    }

    private User user(String email, String userpass) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(userpass);
        user.setRole(Role.USER);
        return user;
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
        LocalDateTime departureDate = getDatePlusHours(counter, today);
        flight.setDepartureDate(departureDate);
        LocalDateTime arrivalDate = getDatePlusMinutes(counter, departureDate);
        flight.setArrivalDate(arrivalDate);
        long between = MINUTES.between(departureDate, arrivalDate);
        flight.setDuration((int) between);
        double price = random.nextDouble() * MAX_PRICE;
        flight.setPrice(Math.round(price * 100d) / 100d);
        return flight;
    }

    private LocalDateTime getDatePlusDays(int days, LocalDateTime time) {
        return LocalDateTime.from(time)
                            .plusDays(days);
    }

    private LocalDateTime getDatePlusHours(int hours, LocalDateTime time) {
        return LocalDateTime.from(time)
                            .plusHours(hours);
    }

    private LocalDateTime getDatePlusMinutes(int minutes, LocalDateTime time) {
        return LocalDateTime.from(time)
                            .plusMinutes(minutes);
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