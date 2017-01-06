package com.service;

import com.domain.CurrentUser;
import com.domain.Customer;

/**
 * Created by Michał on 2016-11-20.
 */
public interface CustomerService {
    boolean canAccessCustomer(CurrentUser currentUser, Long userId);

    Customer create(Customer customer);

    Customer findCustomerByUserId(Long id);

    void delete(Long id);
}