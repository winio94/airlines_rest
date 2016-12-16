package com.controller;

import com.domain.Customer;
import com.service.CustomerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.security.Principal;

/**
 * Created by Michał on 2016-12-12.
 */
@RestController
public class CustomerController {

    @Inject
    private CustomerService customerService;

    @PreAuthorize("@customerServiceImpl.canAccessCustomerPage(principal, #id)")
    @RequestMapping("/customers/{id}")
    public Customer getUserPage(Principal principal, @PathVariable Long id) {
        return customerService.findCustomerByUserId(id);
    }
}