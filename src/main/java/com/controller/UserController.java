package com.controller;

import com.domain.User;
import com.service.UserService;
import com.service.UserValidator;
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
    private UserValidator userValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}