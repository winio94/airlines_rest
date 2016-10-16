package com.configuration;

import com.filter.CORSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.filter.CorsFilter;

/**
 * Created by Michał on 2016-10-16.
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CORSFilter corsFliter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("USER", "ADMIN").and()
                .withUser("user").password("user").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(corsFliter, CorsFilter.class)
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