package com.util.constants;

/**
 * Created by Michał on 2016-11-11.
 */
public interface MessageConstants {
    ///////////////////////////////SUCCESS MESSAGES//////////////////////////////////////
    public static final String TICKET_MAIL_SUBJECT = "Airline Reservation System. Ticket FOR ";

    ////////////////////////////////ERROR MESSAGES///////////////////////////////////////
    public static final String RESERVATION_VALIDATION_MESSAGE = "Could not create reservation. Either contact information or reservation date was missing.";
    public static final String NULL_RESERVATION_MESSAGE = "Could not persist NULL reservation.";
}