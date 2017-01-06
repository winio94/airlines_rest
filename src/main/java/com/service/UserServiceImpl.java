package com.service;

import com.domain.Role;
import com.domain.User;
import com.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by Michał on 2016-11-14.
 */
@Named
public class UserServiceImpl implements UserService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private BCryptPasswordEncoder encoder;

    @Override
    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User create(User user) {
        User u = new User();
        u.setEmail(user.getEmail());
        u.setPassword(encoder.encode(user.getPassword()));
        u.setRole(Role.USER);
        return userRepository.save(u);
    }
}