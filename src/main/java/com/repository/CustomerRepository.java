package com.repository;

import com.domain.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created by Michał on 2016-10-02.
 */

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    Customer findByLastName(@Param("name") String name);
}
