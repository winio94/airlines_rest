package com.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michał on 2016-11-15.
 */
public class ValidationError {
    private List<FieldError> errors = new ArrayList<>();

    public ValidationError(List<FieldError> errors) {
        this.errors = errors;
    }

    public void addFieldError(String field, String errorMessage) {
        FieldError fieldError = new FieldError(field, errorMessage);
        errors.add(fieldError);
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldError> errors) {
        this.errors = errors;
    }
}