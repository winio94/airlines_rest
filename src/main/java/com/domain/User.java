package com.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * Created by Michał on 2016-11-14.
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String userName;

    @NotEmpty
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}