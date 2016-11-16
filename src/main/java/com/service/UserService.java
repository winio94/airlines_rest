package com.service;

import com.domain.User;

import java.util.List;

/**
 * Created by Michał on 2016-11-14.
 */
public interface UserService {
    User getUserById(Long id);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    User create(User user);
}