package com.service;

import com.domain.CurrentUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Michał on 2016-11-14.
 */
@Named
public class CurrentUserDetailsService implements UserDetailsService {

    @Inject
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CurrentUser(userService.getUserByEmail(username));
    }
}