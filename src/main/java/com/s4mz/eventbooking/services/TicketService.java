package com.s4mz.eventbooking.services;

import com.s4mz.eventbooking.domain.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TicketService {

    Page<Ticket> listTicketsForUser(UUID userId, Pageable pageable);
    Optional<Ticket> getTicketForUser(UUID userID, UUID ticketId);
}
