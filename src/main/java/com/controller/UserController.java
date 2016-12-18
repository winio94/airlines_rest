package com.controller;

import com.domain.Customer;
import com.domain.User;
import com.service.CustomerService;
import com.service.UserService;
import com.service.UserValidator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by Michał on 2016-11-14.
 */
@RestController
public class UserController {

    @Inject
    private UserService userService;

    @Inject
    private CustomerService customerService;

    @Inject
    private UserValidator userValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @PostMapping(value = "/users")
    @Transactional
    public User createUser(@Valid @RequestBody User user) {
        User entity = userService.create(user);
        createCustomerFor(entity);
        return entity;
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    private void createCustomerFor(User u) {
        Customer customer = new Customer();
        customer.setUser(u);
        customerService.create(customer);
    }
}