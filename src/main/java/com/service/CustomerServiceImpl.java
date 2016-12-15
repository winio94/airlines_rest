package com.service;

import com.domain.CurrentUser;
import com.domain.Customer;
import com.repository.CustomerRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;

/**
 * Created by Michał on 2016-11-20.
 */
@Named
public class CustomerServiceImpl implements CustomerService {

    private static final String ADMIN_AUTHORITY = "ADMIN";

    @Inject
    private CustomerRepository customerRepository;

    @Override
    public boolean canAccessCustomerPage(CurrentUser currentUser, Long userId) {
        return Objects.nonNull(currentUser) && (isAdmin(currentUser) || currentUser.getId().equals(userId));
    }

    private boolean isAdmin(CurrentUser currentUser) {
        return currentUser.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(ADMIN_AUTHORITY));
    }

    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer findCustomerByUserId(Long id) {
        return customerRepository.findCustomerByUserId(id);
    }
}