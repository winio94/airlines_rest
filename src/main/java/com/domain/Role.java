package com.domain;

/**
 * Created by Michał on 2016-11-14.
 */
public enum Role {
    USER("user"), ADMIN("admin");

    private String value;

    Role(String value) {
        this.value = value;
    }
}