package com.controller;

import com.domain.*;
import com.service.CustomerService;
import com.service.PasswordValidator;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by Michał on 2016-12-12.
 */
@RepositoryRestController
public class CustomerController {

    @Inject
    private CustomerService customerService;

    @Inject
    private PasswordValidator passwordValidator;

    @Inject
    private SessionRegistry sessionRegistry;

    @Inject
    private BCryptPasswordEncoder encoder;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(passwordValidator);
    }

    @PreAuthorize("@customerServiceImpl.canAccessCustomer(principal, #id)")
    @GetMapping("/customers/{id}")
    public @ResponseBody PersistentEntityResource getUserPage(Principal principal, @PathVariable Long id,
                                                              PersistentEntityResourceAssembler assembler) {
        return assembler.toResource(customerService.findCustomerByUserId(id));
    }

    @Transactional
    @PreAuthorize("@customerServiceImpl.canAccessCustomer(principal, #id)")
    @PutMapping(value = "/customers/{id}")
    public ResponseEntity<FieldError> updatePassword(Principal principal, @PathVariable Long id, @Valid @RequestBody PasswordChangeDto passwordChangeDto) throws ServletException {
        Customer customer = customerService.findCustomerByUserId(id);
        User user = customer.getUser();
        if (haveDifferentPasswords(passwordChangeDto, user)) {
            return new ResponseEntity<>(new FieldError("oldPassword", "Wrong old password."), HttpStatus.BAD_REQUEST);
        }
        user.setPassword(encoder.encode(passwordChangeDto.getNewPassword()));
        expireAllSessionsForPrincipal(principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("@customerServiceImpl.canAccessCustomer(principal, #id)")
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(Principal principal, @PathVariable Long id) {
        customerService.delete(id);
    }

    private boolean haveDifferentPasswords(@Valid @RequestBody PasswordChangeDto passwordChangeDto, User user) {
        return !encoder.matches(passwordChangeDto.getOldPassword(), user.getPassword());
    }

    private void expireAllSessionsForPrincipal(Principal principal) {
        CurrentUser currentUser = (CurrentUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        allPrincipals.stream()
                     .map(CurrentUser.class::cast)
                     .filter(sameUser(currentUser))
                     .forEach(expireSession());
    }

    private Predicate<CurrentUser> sameUser(CurrentUser currentUser) {
        return c -> c.getUser().getEmail().equals(currentUser.getUser().getEmail());
    }

    private Consumer<CurrentUser> expireSession() {
        return currentUser -> sessionRegistry.getAllSessions(currentUser, false)
                                             .forEach(SessionInformation::expireNow);
    }
}