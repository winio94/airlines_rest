package com.service;

import com.domain.ValidationError;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Created by Michał on 2016-11-15.
 */
@ControllerAdvice
public class RestErrorHandler {

    @Inject
    private MessageSource messageSource;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationError processValidationError(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();
        return processFieldErrors(errors);
    }

    private ValidationError processFieldErrors(List<FieldError> errors) {
        return new ValidationError(getFieldErrorsFrom(errors));
    }

    private List<com.domain.FieldError> getFieldErrorsFrom(List<FieldError> errors) {
        return Optional.ofNullable(errors)
                       .orElse(emptyList())
                       .stream()
                       .map(toFieldError())
                       .collect(toList());
    }

    private Function<FieldError, com.domain.FieldError> toFieldError() {
        return fieldError -> new com.domain.FieldError(fieldError.getField(), fieldError.getDefaultMessage());
    }
}