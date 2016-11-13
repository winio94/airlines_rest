package com.service;

import com.domain.Ticket;
import com.repository.TicketRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Michał on 2016-11-13.
 */

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceImplTest {

    @Test
    public void shouldSaveGivenTicketIntoRepository() throws Exception {
        Ticket ticket = mock(Ticket.class);
        TicketRepository ticketRepository = mock(TicketRepository.class);
        TicketServiceImpl ticketService = new TicketServiceImpl(ticketRepository);

        ticketService.save(ticket);

        verify(ticketRepository).save(ticket);
    }
}