package com.domain;

/**
 * Created by Michał on 2016-11-15.
 */
public class FieldError {
    private String property;
    private String message;

    public FieldError(String property, String message) {
        this.property = property;
        this.message = message;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}