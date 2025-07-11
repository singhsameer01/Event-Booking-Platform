package com.s4mz.eventbooking.services;

import com.s4mz.eventbooking.domain.entities.Ticket;

import java.util.UUID;

public interface TicketTypeService {
    Ticket purchaseTicket(UUID userId, UUID ticketTypeId);
}
