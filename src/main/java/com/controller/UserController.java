package com.controller;

import com.domain.User;
import com.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by Michał on 2016-11-14.
 */
@RestController
public class UserController {

    @Inject
    private UserService userService;

    @PostMapping(value = "/users", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}