package com.service;

import com.domain.Contact;
import com.domain.Passenger;
import com.domain.Reservation;
import com.domain.Ticket;
import com.repository.TicketRepository;
import com.util.constants.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.util.constants.NumberConstants.SENDING_MAIL_INTERVAL;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

/**
 * Created by Michał on 2016-11-12.
 */
@Named
public class MailSendTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSendTask.class);

    @Inject
    private MailSenderService mailSender;

    @Inject
    private TicketRepository ticketRepository;

    @Transactional
    @Scheduled(fixedRate = SENDING_MAIL_INTERVAL)
    public void createMailTask() {
        LOGGER.debug("Sending email task starter.");
        ofNullable(newTickets())
                .orElse(emptyList())
                .forEach(this::sendMailWithTicket);
        LOGGER.debug("Sending email task ended.");
    }

    private List<Ticket> newTickets() {
        return ticketRepository.findTicketsByWasSentFalse();
    }

    private void sendMailWithTicket(Ticket ticket) {
        try {
            Reservation reservation = ticket.getReservation();
            mailSender.send(getAddress(reservation), getSubject(reservation), reservation.toString());
            ticket.setWasSent(TRUE);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String getSubject(Reservation reservation) {
        Set<Passenger> passengers = reservation.getPassengers();
        Iterator<Passenger> iterator = passengers.iterator();
        if (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            return MessageConstants.TICKET_MAIL_SUBJECT + passenger.getFirstName() + " " + passenger.getLastName();
        }
        return "";
    }

    private InternetAddress getAddress(Reservation reservation) throws AddressException {
        Contact contact = reservation.getContact();
        String email = contact.getEmail();
        return new InternetAddress(email);
    }
}