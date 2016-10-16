package com.service;

import com.domain.Customer;
import com.domain.Flight;
import com.repository.CustomerRepository;
import com.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;

import static java.lang.String.valueOf;

/**
 * Created by Michał on 2016-10-16.
 */

@Component
public class InitializationService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FlightRepository flightRepository;

    @PostConstruct
    private void initialize() {
        for (int i = 0; i < 10; i++) {
            customerRepository.save(customer(i));
            flightRepository.save(flight(i));
        }
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