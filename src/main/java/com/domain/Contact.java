package com.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Michał on 2016-11-10.
 */
@Embeddable
@Access(AccessType.FIELD)
public class Contact {

    @NotEmpty
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = "Wrong email value.")
    private String email;

    @NotEmpty
    @Size(min = 9, max = 12)
    @Pattern(regexp = "^\\+(?:[0-9] ?){9,12}[0-9]$" , message = "Phone number must have directional at the beginning.")
    private String phone;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
