package com.s4mz.eventbooking.services.impl;

import com.s4mz.eventbooking.domain.entities.Ticket;
import com.s4mz.eventbooking.repositories.TicketRepository;
import com.s4mz.eventbooking.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public Page<Ticket> listTicketsForUser(UUID userId, Pageable pageable) {
        return ticketRepository.findByPurchaserId(userId, pageable);
    }

    @Override
    public Optional<Ticket> getTicketForUser(UUID userID, UUID ticketId) {
        return ticketRepository.findByIdAndPurchaserId(ticketId, userID);
    }
}
