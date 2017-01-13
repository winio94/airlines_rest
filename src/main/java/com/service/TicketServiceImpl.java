package com.service;

import com.domain.Ticket;
import com.repository.TicketRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by Michał on 2016-11-12.
 */
@Named
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Inject
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> findTicketsByWasSentFalse() {
        return ticketRepository.findTicketsByWasSentFalse();
    }

    @Override
    public Ticket findTicketByReservationId(Long id) {
        return ticketRepository.findTicketByReservationId(id);
    }

    @Override
    public void delete(Ticket ticket) {
        ticketRepository.delete(ticket);
    }
}