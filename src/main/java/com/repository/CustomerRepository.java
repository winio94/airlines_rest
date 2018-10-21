package com.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

import com.domain.Customer;

/**
 * Created by Michał on 2016-10-02.
 */
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
    Customer findByLastName(@Param("lastName") String lastName);

    Customer findByFirstName(@Param("firstName") String firstName);

    @Query("SELECT c FROM Customer c WHERE c.firstName LIKE CONCAT('%',:firstName,'%') ORDER BY c.firstName")
    List<Customer> findCustomersByPartOfFirtsName(@Param("firstName") String firstName);

    @Query("SELECT c FROM Customer c WHERE c.lastName LIKE CONCAT('%',:lastName,'%') ORDER BY c.lastName")
    List<Customer> findCustomersByPartOfLastName(@Param("lastName") String lastName);

    Customer findCustomerByUserId(@Param("id") Long id);

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    Iterable<Customer> findAll();

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    Iterable<Customer> findAll(Sort sort);

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    Page<Customer> findAll(Pageable pageable);
}