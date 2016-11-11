package com.domain;

/**
 * Created by Michał on 2016-11-11.
 */
public final class ContactBuilder {
    private String email;
    private String phone;

    private ContactBuilder() {
    }

    public static ContactBuilder aContact() {
        return new ContactBuilder();
    }

    public ContactBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public ContactBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Contact build() {
        Contact contact = new Contact();
        contact.setEmail(email);
        contact.setPhone(phone);
        return contact;
    }
}
