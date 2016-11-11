package com.domain.listener;

import com.domain.Contact;
import com.domain.ContactBuilder;
import com.domain.Reservation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.DigestUtils;

import javax.persistence.PersistenceException;
import javax.validation.ValidationException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Michał on 2016-11-11.
 */
public class ReservationListenerTest {

    private static final String PROPER_PHONE = "+48666777888";
    private static final String PROPER_EMAIL = "aaa123@xxx.com";
    private ReservationListener reservationListener;
    private Reservation reservation;

    @Before
    public void setUp() throws Exception {
        reservationListener = new ReservationListener();
        reservation = new Reservation();
        setUpReservationWithDate(new Date());
        setUpReservationWithContact(contact(PROPER_EMAIL, PROPER_PHONE));
    }

    @Test(expected = PersistenceException.class)
    public void shouldThrowValidationExceptionWhenGivenNullReservation() throws Exception {
        reservationListener.prePersist(null);
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowValidationExceptionWhenGivenReservationDoesNotHaveContactInformation() throws Exception {
        setUpReservationWithoutContact();

        reservationListener.prePersist(reservation);
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowValidationExceptionWhenGivenReservationHasNullDate() throws Exception {
        setUpReservationWithoutDate();

        reservationListener.prePersist(reservation);
    }

    @Test
    public void shouldCreateCodeForReservationBasedOnGivenContactAndDate() throws Exception {
        reservationListener.prePersist(reservation);

        assertNotNull(reservation.getReservationCode());
        assertEquals(md5ReservationCode(), reservation.getReservationCode());
    }

    private void setUpReservationWithDate(Date date) {
        reservation.setReservationDate(date);
    }

    private void setUpReservationWithContact(Contact contact) {
        reservation.setContact(contact);
    }

    private Contact contact(String email, String phone) {
        return ContactBuilder.aContact()
                             .withEmail(email)
                             .withPhone(phone)
                             .build();
    }

    private void setUpReservationWithoutContact() {
        setUpReservationWithContact(null);
    }

    private void setUpReservationWithoutDate() {
        setUpReservationWithDate(null);
    }

    private String md5ReservationCode() {
        Date reservationDate = reservation.getReservationDate();
        Contact contact = reservation.getContact();
        String email = contact.getEmail();
        String phone = contact.getPhone();
        String codeBeforeHash = email + phone + reservationDate.toString();
        return DigestUtils.md5DigestAsHex(codeBeforeHash.getBytes());
    }
}