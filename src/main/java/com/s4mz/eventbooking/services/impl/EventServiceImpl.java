package com.s4mz.eventbooking.services.impl;

import com.s4mz.eventbooking.domain.CreateEventRequest;
import com.s4mz.eventbooking.domain.UpdateEventRequest;
import com.s4mz.eventbooking.domain.UpdateTicketTypeRequest;
import com.s4mz.eventbooking.domain.entities.Event;
import com.s4mz.eventbooking.domain.entities.EventStatusEnum;
import com.s4mz.eventbooking.domain.entities.TicketType;
import com.s4mz.eventbooking.domain.entities.User;
import com.s4mz.eventbooking.exceptions.EventUpdateException;
import com.s4mz.eventbooking.exceptions.TicketTypeNotFoundException;
import com.s4mz.eventbooking.exceptions.UserNotFoundException;
import com.s4mz.eventbooking.repositories.EventRepository;
import com.s4mz.eventbooking.repositories.UserRepository;
import com.s4mz.eventbooking.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    @Override
    @Transactional
    public Event createEvent(UUID organizerId, CreateEventRequest request) {
        User user=userRepository.findById(organizerId).orElseThrow(
                ()->new UserNotFoundException("User with id: "+organizerId+" not found")
        );

        Event event=new Event();

        List<TicketType> ticketTypes=request.getTicketTypes().stream().map(
                ticketType->{
                    TicketType typeToCreate=new TicketType();
                    typeToCreate.setName(ticketType.getName());
                    typeToCreate.setPrice(ticketType.getPrice());
                    typeToCreate.setTicketsAvailable(ticketType.getTicketsAvailable());
                    typeToCreate.setDescription(ticketType.getDescription());
                    typeToCreate.setEvent(event);
                    return typeToCreate;
                }
        ).toList();


        event.setName(request.getName());
        event.setStart(request.getStart());
        event.setEnd(request.getEnd());
        event.setVenue(request.getVenue());
        event.setSalesStart(request.getSalesStart());
        event.setSalesEnd(request.getSalesEnd());
        event.setStatus(request.getStatus());
        event.setOrganizer(user);
        event.setTicketTypes(ticketTypes);
        return eventRepository.save(event);
    }

    @Override
    public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
        return eventRepository.findByOrganizerId(organizerId,pageable);
    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID eventId) {
        return eventRepository.findByIdAndOrganizerId(eventId,organizerId);
    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID organizerId, UUID eventId, UpdateEventRequest request) {
//        if(request.getId()==null){
//            throw new EventUpdateException("Event id cannot be null");
//        }
//        if(request.getId().equals(eventId) ){
//            throw new EventUpdateException("Cannot update event with id: "+eventId);
//        }
        Event existingEvent=eventRepository.findByIdAndOrganizerId(eventId,organizerId)
                .orElseThrow(
                        ()->new EventUpdateException("Event with id: "+eventId+" not found")
                );
        existingEvent.setName(request.getName());
        existingEvent.setStart(request.getStart());
        existingEvent.setEnd(request.getEnd());
        existingEvent.setVenue(request.getVenue());
        existingEvent.setSalesStart(request.getSalesStart());
        existingEvent.setSalesEnd(request.getSalesEnd());
        existingEvent.setStatus(request.getStatus());

        Set<UUID> requestTicketTypeIds=request
                .getTicketTypes()
                .stream()
                .map(UpdateTicketTypeRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEvent.getTicketTypes().removeIf(existingTicketType->
                !requestTicketTypeIds.contains(existingTicketType.getId()));

        Map<UUID,TicketType> existingTicketTypesIds= existingEvent.getTicketTypes().stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for(UpdateTicketTypeRequest ticketTypeRequest:request.getTicketTypes()){
            if (ticketTypeRequest.getId()==null){
                TicketType ticketType=new TicketType();
                ticketType.setName(ticketTypeRequest.getName());
                ticketType.setPrice(ticketTypeRequest.getPrice());
                ticketType.setTicketsAvailable(ticketTypeRequest.getTicketsAvailable());
                ticketType.setDescription(ticketTypeRequest.getDescription());
                ticketType.setEvent(existingEvent);
                existingEvent.getTicketTypes().add(ticketType);
            }
            else if(existingTicketTypesIds.containsKey(ticketTypeRequest.getId())){
                TicketType existingTicketType=existingTicketTypesIds.get(ticketTypeRequest.getId());
                existingTicketType.setName(ticketTypeRequest.getName());
                existingTicketType.setPrice(ticketTypeRequest.getPrice());
                existingTicketType.setDescription(ticketTypeRequest.getDescription());
                existingTicketType.setTicketsAvailable(ticketTypeRequest.getTicketsAvailable());
            }
            else {
                throw  new TicketTypeNotFoundException("Ticket type with id: "+ticketTypeRequest.getId()+" not found");
            }
        }
        return eventRepository.save(existingEvent);
    }

    @Override
    @Transactional
    public void deleteEventForOrganizer(UUID organizerId, UUID eventId) {
        getEventForOrganizer(organizerId,eventId).ifPresent(eventRepository::delete);
    }

    @Override
    public Page<Event> listPublishedEvents(Pageable pageable) {
        return eventRepository.findByStatus(EventStatusEnum.PUBLISHED,pageable);
    }

    @Override
    public Page<Event> searchPublishedEvents(String searchTerm, Pageable pageable) {
        return eventRepository.searchEvents(searchTerm,pageable);
    }

    @Override
    public Optional<Event> getPublishedEvent(UUID eventId) {
        return eventRepository.findByIdAndStatus(eventId,EventStatusEnum.PUBLISHED);
    }
}
