package com.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * Created by Michał on 2016-10-21.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CorsFilter corsFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("admin").password("admin").roles("USER", "ADMIN").and()
            .withUser("user").password("user").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers()
            .frameOptions()
            .sameOrigin()//h2 console problem due to set 'X-Frame-Options' to 'DENY'.
            .and()
            .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.PUT, "/flights").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.POST, "/flights").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.DELETE, "/flights").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.GET, "/flights/**").permitAll()
            .antMatchers(HttpMethod.GET, "/customers").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.POST, "/customers").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.PUT, "/customers").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.DELETE, "/customers").access("hasRole('ROLE_ADMIN')")
            .antMatchers(HttpMethod.POST, "/reservations").permitAll()
            .antMatchers(HttpMethod.GET, "/airports/**").permitAll()
            .antMatchers(HttpMethod.GET, "/user").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/*").permitAll()
            .antMatchers("/console/**").permitAll()
            .antMatchers("/**").denyAll()
            .and()
            .httpBasic();
    }
}