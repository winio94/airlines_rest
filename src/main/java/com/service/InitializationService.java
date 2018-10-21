package com.service;

import static java.lang.String.valueOf;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.domain.Airport;
import com.domain.Customer;
import com.domain.Flight;
import com.domain.FlightClass;
import com.domain.Role;
import com.domain.Route;
import com.domain.User;
import com.service.flight.FlightService;
import com.service.route.RouteService;
import com.service.shortest_path.DataReader;
import com.service.shortest_path.DistanceCalculator;

/**
 * Created by Michał on 2016-10-16.
 */

@Named
public class InitializationService {

    private static final int MAX_PRICE = 1000;
    private static final int CUSTOMER_AMOUNT = 10;
    private static final int FLIGHT_AMOUNT = 10000;
    private static final String CITIES_FILE_PATH = "bigCities.txt";
    private static final String PASSWORD = "password";
    private static final String EMAIL_PREFIX = "email";
    private static final String EMAIL_SUFFIX = "@gmail.com";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final Logger LOGGER = LoggerFactory.getLogger(InitializationService.class);
    private static final double KM_PER_SECOND = 4.5;
    private final Random random = new Random();
    private List<String> cities = new ArrayList<>();
    private List<Airport> airports = new ArrayList<>();
    private List<Route> routes = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();

    @Inject
    private CustomerService customerService;

    @Inject
    private FlightService flightService;

    @Inject
    private RouteService routeService;

    @Inject
    private AirportService airportService;

    @Inject
    private UserService userService;

    @Inject
    private BCryptPasswordEncoder encoder;

    private final DistanceCalculator distanceCalculator = new DistanceCalculator();

    public InitializationService() throws IOException {
    }

    @PostConstruct
    private void initialize() throws IOException, ClassNotFoundException {
        initializeAdmin();
//        initializeCities();
        initializeAirports();
        initializeRoutes();
        initializeCustomers();
        initializeFlights();
    }

    private void initializeAdmin() {
        User admin = admin();
        Customer adminCustomer = new Customer();
        adminCustomer.setUser(admin);
        customerService.create(adminCustomer);
    }

    private void initializeAirports() throws IOException, ClassNotFoundException {
        airports = DataReader.readObjects(DataReader.class.getClassLoader().getResource("airports.ser").getPath());
        airports.forEach(a -> airportService.create(a));
    }

    private void initializeRoutes() throws IOException, ClassNotFoundException {
        List<Route> routes = DataReader.readObjects(DataReader.class.getClassLoader().getResource("polishRoutes.ser").getPath());
        routes.forEach(r -> routeService.create(r));
    }

    private void initializeCustomers() {
        for (int i = 0; i < CUSTOMER_AMOUNT; i++) {
            customerService.create(customer(i));
        }
    }

    private void initializeFlights() {
        FlightClass[] flightClasses = FlightClass.values();
        for (int i = 0; i < FLIGHT_AMOUNT; i++) {
            Flight flight = flight(i, flightClasses);
            flightService.create(flight);
        }

//        for (Route r : routes) {
//            for (int i = 0; i < 5; i++) {
//                try {
//                    Flight flight = flight(r, i, flightClasses);
//                    flightService.create(flight);
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//        }
    }

    private Airport getDestinationAirport(Airport source) {
        Airport destination = airports.get(random.nextInt(airports.size()));
        while (Objects.equals(destination.getCity(), source.getCity())) {
            destination = airports.get(random.nextInt(airports.size()));
        }
        if (source.getCity().equalsIgnoreCase("Krakow") && destination.getCity().equalsIgnoreCase("Warsaw")) {
            return getDestinationAirport(source);
        }
        return destination;
    }

    private User admin() {
        User admin = new User();
        admin.setEmail("admin@admin.com");
        admin.setPassword(encoder.encode("adminpass"));
        admin.setRole(Role.ADMIN);
        return admin;
    }

    private User user(String email, String userpass) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(userpass));
        user.setRole(Role.USER);
        return user;
    }

    private Airport airport(int i) {
        Airport airport = new Airport();
        airport.setCity(cities.get(i));
        return airport;
    }

    public long getDistance(Flight f) {
        return distanceCalculator.distance(f.getFrom().getLatitude(), f.getTo().getLatitude(), f.getFrom().getLongitude(), f.getTo().getLongitude());
    }

    private Flight flight(int counter, FlightClass[] flightClasses) {
        Flight flight = new Flight();
        Airport sourceAirport = airports.get(random.nextInt(airports.size()));
        Airport destinationAirport = getDestinationAirport(sourceAirport);
        flight.setFrom(sourceAirport);
        flight.setTo(destinationAirport);
        FlightClass flightClass = flightClasses[random.nextInt(flightClasses.length)];
        flight.setFlightClass(flightClass);
        flight.setFlightNumber(valueOf(counter));

        int randomMinutesNumberLessThan12Hours = getRanomMinutesLessThan(12 * 60);
        LocalDateTime today = LocalDateTime.now();
        int duration = getaDurationInSeconds(sourceAirport, destinationAirport);
        LocalDateTime departureDate = getDatePlusMinutes(randomMinutesNumberLessThan12Hours, today);
        LocalDateTime arrivalDate = getDatePlusSeconds(duration, departureDate);
        flight.setDepartureDate(departureDate);
        flight.setDuration(duration);
        flight.setArrivalDate(arrivalDate);

        double price = random.nextDouble() * MAX_PRICE;
        flight.setPrice(Math.round(price * 100d) / 100d);
        return flight;
    }

    private int getRanomMinutesLessThan(int maxNumber) {
        return ThreadLocalRandom.current().nextInt(maxNumber);
    }

    private Flight flight(Route route, int i, FlightClass[] flightClasses) {
        Flight flight = new Flight();
        Airport sourceAirport = airports.get(route.getSourceAirportId());
        Airport destinationAirport = airports.get(route.getSourceAirportId());
        flight.setFrom(sourceAirport);
        flight.setTo(destinationAirport);
        FlightClass flightClass = flightClasses[random.nextInt(flightClasses.length)];
        flight.setFlightClass(flightClass);
        flight.setFlightNumber(valueOf(route.getId() * i));

        LocalDateTime today = LocalDateTime.now();
        int duration = getaDurationInSeconds(sourceAirport, destinationAirport);
        LocalDateTime departureDate = getDatePlusSeconds(Math.round(route.getId() / i), today);
        LocalDateTime arrivalDate = getDatePlusSeconds(duration, departureDate);
        flight.setDepartureDate(departureDate);
        flight.setDuration(duration);
        flight.setArrivalDate(arrivalDate);

        double price = random.nextDouble() * MAX_PRICE;
        flight.setPrice(Math.round(price * 100d) / 100d);
        return flight;
    }

    private int getaDurationInSeconds(Airport sourceAirport, Airport destinationAirport) {
        return (int) (distanceCalculator.distance(sourceAirport.getLatitude(), destinationAirport.getLatitude(), sourceAirport.getLongitude(), destinationAirport.getLongitude()) * KM_PER_SECOND);
    }

    private LocalDateTime getDatePlusDays(int days, LocalDateTime time) {
        return LocalDateTime.from(time)
                            .plusDays(days);
    }

    private LocalDateTime getDatePlusHours(int minutes, LocalDateTime time) {
        return LocalDateTime.from(time)
                            .plusMinutes(minutes);
    }

    private LocalDateTime getDatePlusMinutes(long minutes, LocalDateTime time) {
        return LocalDateTime.from(time)
                            .plusMinutes(minutes);
    }

    private LocalDateTime getDatePlusSeconds(int seconds, LocalDateTime time) {
        return LocalDateTime.from(time)
                            .plusSeconds(seconds);
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