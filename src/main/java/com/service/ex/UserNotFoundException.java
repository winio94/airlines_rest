package com.service.ex;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(String.format("Could not find User with id: %s", id));
    }
}
