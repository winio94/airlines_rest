package com.repository;

import com.domain.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Michał on 2016-10-02.
 */
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Customer findByLastName(@Param("lastName") String lastName);

    Customer findByFirstName(@Param("firstName") String firstName);

    @Query("SELECT c FROM Customer c WHERE c.firstName LIKE CONCAT('%',:firstName,'%') ORDER BY c.firstName")
    List<Customer> findCustomersByPartOfFirtsName(@Param("firstName") String firstName);

    @Query("SELECT c FROM Customer c WHERE c.lastName LIKE CONCAT('%',:lastName,'%') ORDER BY c.lastName")
    List<Customer> findCustomersByPartOfLastName(@Param("lastName") String lastName);
}