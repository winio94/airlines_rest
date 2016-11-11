package com.domain.listener;

import com.domain.Contact;
import com.domain.Reservation;

import javax.persistence.PersistenceException;
import javax.persistence.PrePersist;
import javax.validation.ValidationException;
import java.util.Date;
import java.util.Objects;

import static com.util.StringUtil.md5HashedString;
import static com.util.constants.Constants.NULL_RESERVATION_MESSAGE;
import static com.util.constants.Constants.RESERVATION_VALIDATION_MESSAGE;

/**
 * Created by Michał on 2016-11-11.
 */
public class ReservationListener {

    @PrePersist
    void prePersist(Reservation reservation) {
        throwExceptionWhenInvalid(reservation);
        reservation.setReservationCode(createReservationCode(reservation));
    }

    private void throwExceptionWhenInvalid(Reservation reservation) {
        if (Objects.isNull(reservation)) {
            throw new PersistenceException(NULL_RESERVATION_MESSAGE);
        }
        if (!isProperReservation(reservation)) {
            throw new ValidationException(RESERVATION_VALIDATION_MESSAGE);
        }
    }

    private boolean isProperReservation(Reservation reservation) {
        return Objects.nonNull(reservation.getContact()) && Objects.nonNull(reservation.getReservationDate());
    }

    private String createReservationCode(Reservation reservation) {
        Contact contact = reservation.getContact();
        Date reservationDate = reservation.getReservationDate();
        String reservationCodeBeforeHash = contact.getEmail() + contact.getPhone() + reservationDate.toString();
        return md5HashedString(reservationCodeBeforeHash);
    }
}