package com.s4mz.eventbooking.repositories;

import com.s4mz.eventbooking.domain.entities.TicketValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TicketValidationRepository extends JpaRepository<TicketValidation, UUID> {
}
