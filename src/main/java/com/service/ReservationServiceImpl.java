package com.service;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import javax.inject.Inject;
import javax.inject.Named;

import com.domain.CurrentUser;
import com.domain.Customer;
import com.domain.Reservation;
import com.repository.ReservationRepository;

/**
 * Created by Michał on 2016-12-18.
 */

@Named
public class ReservationServiceImpl implements ReservationService {

    @Inject
    private ReservationRepository reservationRepository;

    @Inject
    private CustomerService customerService;

    public Set<Reservation> findReservationsByCustomerId(Long customerId) {
        return reservationRepository.findReservationsByCustomerId(customerId);
    }

    @Override
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public Reservation findReservationByReservationCode(String reservationCode) {
        return reservationRepository.findReservationByReservationCode(reservationCode);
    }

    @Override
    public boolean canDeletedReservation(CurrentUser currentUser, Long reservationId) {
        if (areNotNull(currentUser, reservationId)) {
            Customer customer = customerService.findCustomerByUserId(currentUser.getId());
            Set<Reservation> reservations = findReservationsByCustomerId(customer.getId());
            if (containsReservationWithId(reservationId, reservations)) {
                return true;
            }
        }
        return false;
    }

    private boolean areNotNull(CurrentUser currentUser, Long reservationId) {
        return Objects.nonNull(currentUser) && Objects.nonNull(reservationId);
    }

    private boolean containsReservationWithId(Long reservationId, Set<Reservation> reservations) {
        return reservations.stream()
                           .anyMatch(hasEqualId(reservationId));
    }

    private Predicate<Reservation> hasEqualId(Long reservationId) {
        return reservation -> reservation.getId().equals(reservationId);
    }
}