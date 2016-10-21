package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CorsFilter;

import java.security.Principal;

/**
 * Created by Michał on 2016-10-01.
 */

@SpringBootApplication
@RestController
public class Application {

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Configuration
    @EnableWebSecurity
    protected class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private CorsFilter corsBean;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("admin").password("admin").roles("USER", "ADMIN").and()
                    .withUser("user").password("user").roles("USER");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(corsBean, ChannelProcessingFilter.class)
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.PUT, "/flights").access("hasRole('ROLE_ADMIN')")
                    .antMatchers(HttpMethod.POST, "/flights").access("hasRole('ROLE_ADMIN')")
                    .antMatchers(HttpMethod.DELETE, "/flights").access("hasRole('ROLE_ADMIN')")
                    .antMatchers(HttpMethod.GET, "/customers").access("hasRole('ROLE_ADMIN')")
                    .antMatchers(HttpMethod.POST, "/customers").access("hasRole('ROLE_ADMIN')")
                    .antMatchers(HttpMethod.PUT, "/customers").access("hasRole('ROLE_ADMIN')")
                    .antMatchers(HttpMethod.DELETE, "/customers").access("hasRole('ROLE_ADMIN')")
                    .antMatchers(HttpMethod.OPTIONS, "/*").permitAll()
                    .and()
                    .httpBasic();
        }
    }
}