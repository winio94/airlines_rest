package com.service;

import com.domain.*;
import com.repository.TicketRepository;
import com.util.constants.MessageConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Michał on 2016-11-12.
 */
@RunWith(MockitoJUnitRunner.class)
public class MailSendTaskTest {

    private static final String EMAIL_ADDRESS = "email@default.com";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";

    @Mock
    private MailSenderService mailSender;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private MailSendTask mailSendTask;

    @Test
    public void shouldSendMailForEveryNewTicket() throws Exception {
        List<Ticket> ticketList = ticketList(5);
        when(ticketRepository.findAll()).thenReturn(ticketList);

        mailSendTask.createMailTask();

        verify(mailSender, times(5)).send(any(), any(), any());
    }

    @Test
    public void shouldSendMailForTicketWithProperArguments() throws Exception {
        String subject = subject(FIRST_NAME, LAST_NAME);
        InternetAddress address = internetAddress(EMAIL_ADDRESS);
        Reservation reservation = reservation(EMAIL_ADDRESS, asList(passenger(FIRST_NAME, LAST_NAME)));
        setUpNewTicketsInDb(ticket(reservation));

        mailSendTask.createMailTask();

        verify(mailSender).send(address, subject, reservation.toString());
    }

    private Reservation reservation(String address, List<Passenger> passengers) {
        return ReservationBuilder.aReservation()
                                 .withContact(contact(address))
                                 .withPassengers(new HashSet<>(passengers))
                                 .build();
    }

    private Contact contact(String address) {
        return ContactBuilder.aContact()
                             .withEmail(address)
                             .build();
    }

    private Passenger passenger(String firstName, String lastName) {
        return PassengerBuilder.aPassenger()
                               .withFirstName(firstName)
                               .withLastName(lastName)
                               .build();
    }

    private InternetAddress internetAddress(String address) throws AddressException {
        return new InternetAddress(address);
    }

    private List<Ticket> ticketList(int capacity) {
        Ticket ticket = mock(Ticket.class);
        Reservation reservation = mock(Reservation.class);
        Contact contact = mock(Contact.class);
        when(contact.getEmail()).thenReturn(EMAIL_ADDRESS);
        when(reservation.getContact()).thenReturn(contact);
        when(ticket.getReservation()).thenReturn(reservation);
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            tickets.add(ticket);
        }
        return tickets;
    }

    private void setUpNewTicketsInDb(Ticket ticket) {
        when(ticketRepository.findAll()).thenReturn(singletonList(ticket));
    }

    private Ticket ticket(Reservation reservation) {
        return TicketBuilder.aTicket()
                            .withReservation(reservation)
                            .build();
    }

    private String subject(String firstName, String lastName) {
        return MessageConstants.TICKET_MAIL_SUBJECT + firstName + " " + lastName;
    }
}