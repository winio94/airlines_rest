package com.service;

import com.domain.*;
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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Michał on 2016-11-12.
 */
@RunWith(MockitoJUnitRunner.class)
public class MailSendTaskTest {

    private static final String EMAIL_ADDRESS = "email@default.com";
    private static final String EMAIL_ADDRESS2 = "email2@default2.com";
    private static final String FIRST_NAME = "John";
    private static final String FIRST_NAME2 = "John";
    private static final String LAST_NAME = "Doe";
    private static final String LAST_NAME2 = "Doe";

    @Mock
    private MailSenderService mailSender;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private MailSendTask mailSendTask;

    @Test
    public void shouldSendMailForEveryNewTicket() throws Exception {
        List<Ticket> ticketList = ticketList(5);
        when(ticketService.findTicketsByWasSentFalse()).thenReturn(ticketList);

        mailSendTask.createMailTask();

        verify(mailSender, times(5)).send(any(), any(), any());
    }

    @Test
    public void shouldSendMailForTicketWithProperArguments() throws Exception {
        String subject = subject(FIRST_NAME, LAST_NAME);
        InternetAddress address = internetAddress(EMAIL_ADDRESS);
        Reservation reservation = reservation(EMAIL_ADDRESS, asList(passenger(FIRST_NAME, LAST_NAME)));
        Ticket ticket = ticket(reservation);
        setUpNewTicketsInDb(ticket);

        mailSendTask.createMailTask();

        verify(mailSender).send(address, subject, reservation.toString());
        assertThat(ticket.getWasSent(), is(true));
    }

    @Test
    public void shouldSetTicketsSendFlagToTrueAfterSendingMessagesForNewTickets() throws Exception {
        Reservation reservation1 = reservation(EMAIL_ADDRESS, singletonList(passenger(FIRST_NAME, LAST_NAME)));
        Reservation reservation2 = reservation(EMAIL_ADDRESS2, singletonList(passenger(FIRST_NAME2, LAST_NAME2)));
        Ticket ticket1 = ticket(reservation1);
        Ticket ticket2 = ticket(reservation2);
        setUpNewTicketsInDb(ticket1, ticket2);

        mailSendTask.createMailTask();

        assertThat(ticket1.getWasSent(), is(true));
        assertThat(ticket2.getWasSent(), is(true));
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

    private void setUpNewTicketsInDb(Ticket... tickets) {
        when(ticketService.findTicketsByWasSentFalse()).thenReturn(asList(tickets));
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