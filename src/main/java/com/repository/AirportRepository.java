package com.repository;

import com.domain.Airport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Michał on 2016-10-23.
 */
public interface AirportRepository extends PagingAndSortingRepository<Airport, Long> {

    //REST request example :
    //http://localhost:8080/airports/search/findAirportsByCityIgnoreCaseContainingOrderByCity?cityName=kr&size=3
    List<Airport> findAirportsByCityIgnoreCaseStartingWithOrderByCity(@Param("cityName") String cityName, Pageable pageRequest);

    List<Airport> findAirportsByCityIgnoreCaseContainingOrderByCity(@Param("cityName") String cityName, Pageable pageRequest);
}