package com.s4mz.eventbooking.services;

import com.s4mz.eventbooking.domain.CreateEventRequest;
import com.s4mz.eventbooking.domain.UpdateEventRequest;
import com.s4mz.eventbooking.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId,CreateEventRequest request);
    Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable);
    Optional<Event> getEventForOrganizer(UUID organizerId, UUID eventId);
    Event updateEventForOrganizer(UUID organizerId, UUID eventId, UpdateEventRequest request);
    void deleteEventForOrganizer(UUID organizerId, UUID eventId);
    Page<Event> listPublishedEvents(Pageable pageable);
    Page<Event> searchPublishedEvents(String query, Pageable pageable);
    Optional<Event> getPublishedEvent(UUID eventId);
}
