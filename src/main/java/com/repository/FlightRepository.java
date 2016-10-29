package com.repository;

import com.domain.Flight;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Michał on 2016-10-02.
 */
public interface FlightRepository extends PagingAndSortingRepository<Flight, Long> {
    Flight findByFlightNumber(@Param("number") String number);

    @Query(value = "SELECT f FROM Flight f WHERE f.from.id = (SELECT id from Airport a WHERE a.city like :cityName)")
    List<Flight> findFlightsByFromCity(@Param("cityName") String cityName);

    List<Flight> findFlightsByFromCityIgnoreCaseContainingAndToCityIgnoreCaseContaining(@Param("from") String from, @Param("to") String to);
}
