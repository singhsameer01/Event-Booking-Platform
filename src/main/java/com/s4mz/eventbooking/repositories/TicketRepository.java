package com.s4mz.eventbooking.repositories;

import com.s4mz.eventbooking.domain.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    int countByTicketTypeId(UUID ticketTypeId);

    Page<Ticket> findByPurchaserId(UUID PurchaserId, Pageable pageable);

    Optional<Ticket> findByIdAndPurchaserId(UUID id,UUID purchaserId);
}
