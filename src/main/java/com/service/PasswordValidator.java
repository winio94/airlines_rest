package com.service;

import com.domain.PasswordChangeDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.inject.Named;

/**
 * Created by Michał on 2017-01-02.
 */
@Named
public class PasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return PasswordChangeDto.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordChangeDto dto = PasswordChangeDto.class.cast(target);
        String newPassword = dto.getNewPassword();
        String newPasswordRetype = dto.getNewPasswordRetype();
        if (!(newPassword.equals(newPasswordRetype))) {
            errors.rejectValue("newPasswordRetype", "newPasswordRetype.value", "Confirmation password was wrong.");
        }
    }
}