package com.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.domain.Airport;

/**
 * Created by Michał on 2016-10-23.
 */
public interface AirportRepository extends PagingAndSortingRepository<Airport, Long> {

    //REST request example :
    //http://localhost:8080/airports/search/findAirportsByMultipleCriteria?cityName=kr&size=3
    @Query("SELECT a FROM Airport a WHERE " +
            "UPPER(a.city) LIKE CONCAT('%', UPPER(?1), '%') " +
            "OR UPPER(a.name) LIKE CONCAT('%', UPPER(?1), '%')" +
            "ORDER BY a.city")
    List<Airport> findAirportsByMultipleCriteria(@Param("cityName") String cityName, Pageable pageRequest);

    List<Airport> findAirportsByCityIgnoreCaseContainingOrderByCity(@Param("cityName") String cityName, Pageable pageRequest);

    Airport findByName(@Param("name") String city);
}