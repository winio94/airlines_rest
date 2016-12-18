package com.service;

import com.domain.CurrentUser;
import com.domain.Customer;
import com.domain.Reservation;
import com.repository.ReservationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static java.util.Collections.emptySet;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Michał on 2016-12-18.
 */
@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceImplTest {

    private static final Long USER_ID = 1L;
    private static final Long RESERVATION_ID = 2L;
    private static final Long CUSTOMER_ID = 3L;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    public void shouldReturnFalseWhenGivenReservationIdIsNull() throws Exception {
        boolean canDeletedReservation = reservationService.canDeletedReservation(mock(CurrentUser.class), null);
        assertFalse(canDeletedReservation);
    }

    @Test
    public void shouldReturnFalseWhenGivenCurrentUserIsNull() throws Exception {
        boolean canDeletedReservation = reservationService.canDeletedReservation(null, RESERVATION_ID);
        assertFalse(canDeletedReservation);
    }

    @Test
    public void shouldReturnFalseWhenGivenUserDidNotCreateReservationWithGivenId() throws Exception {
        CurrentUser currentUser = currentUserWithReservations(emptySet());
        boolean canDeletedReservation = reservationService.canDeletedReservation(currentUser, RESERVATION_ID);

        assertFalse(canDeletedReservation);
    }

    @Test
    public void shouldReturnTrueWhenGivenUserCreatedReservationWithGivenId() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(RESERVATION_ID);
        CurrentUser currentUser = currentUserWithReservations(asSet(reservation));
        boolean canDeletedReservation = reservationService.canDeletedReservation(currentUser, RESERVATION_ID);

        assertTrue(canDeletedReservation);
    }

    private CurrentUser currentUserWithReservations(Set<Reservation> reservations) {
        Customer customer = mock(Customer.class);
        CurrentUser currentUser = mock(CurrentUser.class);
        when(customer.getId()).thenReturn(CUSTOMER_ID);
        when(currentUser.getId()).thenReturn(USER_ID);
        when(customer.getReservations()).thenReturn(reservations);
        when(customerService.findCustomerByUserId(USER_ID)).thenReturn(customer);
        when(reservationRepository.findReservationsByCustomerId(CUSTOMER_ID)).thenReturn(reservations);
        return currentUser;
    }
}