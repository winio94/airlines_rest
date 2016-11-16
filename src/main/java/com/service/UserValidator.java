package com.service;

import com.domain.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;

/**
 * Created by Michał on 2016-11-15.
 */
@Named
public class UserValidator implements Validator {

    @Inject
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        String email = user.getEmail();
        validateEmailUniqueness(email, errors);
    }

    private void validateEmailUniqueness(String email, Errors errors) {
        User userWithSameEmail = userService.getUserByEmail(email);
        if (Objects.nonNull(userWithSameEmail)) {
            errors.rejectValue("email", "email.unique", "This email is used by another user.");
        }
    }
}