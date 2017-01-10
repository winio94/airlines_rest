package com.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SimpleRedirectInvalidSessionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

import javax.inject.Inject;

/**
 * Created by Michał on 2016-10-21.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    private CorsFilter corsFilter;

    @Inject
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
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
            .antMatchers(HttpMethod.GET, "/flights/**").permitAll()
            .antMatchers(HttpMethod.GET, "/customers/**").permitAll()
            .antMatchers(HttpMethod.GET, "/customers").access("hasAuthority('ADMIN')")
            .antMatchers(HttpMethod.GET, "/airports/**").permitAll()
            .antMatchers(HttpMethod.GET, "/user").permitAll()
            .antMatchers(HttpMethod.POST, "/flights").access("hasAuthority('ADMIN')")
            .antMatchers(HttpMethod.POST, "/customers").access("hasAuthority('ADMIN')")
            .antMatchers(HttpMethod.POST, "/reservations").permitAll()
            .antMatchers(HttpMethod.POST, "/users").permitAll()
            .antMatchers(HttpMethod.POST, "/tickets").permitAll()
            .antMatchers(HttpMethod.DELETE, "/flights").access("hasAuthority('ADMIN')")
            .antMatchers(HttpMethod.DELETE, "/customers/*").permitAll()
            .antMatchers(HttpMethod.DELETE, "/customers").access("hasAuthority('ADMIN')")
            .antMatchers(HttpMethod.DELETE, "/reservations/*").permitAll()
            .antMatchers(HttpMethod.PUT, "/flights").access("hasAuthority('ADMIN')")
            .antMatchers(HttpMethod.PUT, "/customers/*").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/*").permitAll()
            .antMatchers("/expired").permitAll()
            .antMatchers("/console/**").permitAll()
            .antMatchers("/**").denyAll()
            .and()
            .httpBasic()
            .and()
            .logout()
            .deleteCookies("JSESSIONID")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/flights")
            .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
            .invalidateHttpSession(true)
            .and()
            .sessionManagement()
            .invalidSessionUrl("/login")
            .maximumSessions(1)
            .expiredUrl("/login")
            .sessionRegistry(sessionRegistry());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public static HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public InvalidSessionStrategy invalidSessionStrategy() {
        SimpleRedirectInvalidSessionStrategy invalidSessionStrategy = new SimpleRedirectInvalidSessionStrategy("/expired");
        invalidSessionStrategy.setCreateNewSession(true);
        return invalidSessionStrategy;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoderIml();
    }

    private class BCryptPasswordEncoderIml extends BCryptPasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            return String.valueOf(rawPassword);
        }
    }
}