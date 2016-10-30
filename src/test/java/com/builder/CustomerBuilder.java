package com.builder;

import com.domain.Customer;

/**
 * Created by Michał on 2016-10-22.
 */
public final class CustomerBuilder {
    private String firstName;
    private String lastName;

    private CustomerBuilder() {
    }

    public static CustomerBuilder aCustomer() {
        return new CustomerBuilder();
    }

    public CustomerBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CustomerBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Customer build() {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        return customer;
    }
}
