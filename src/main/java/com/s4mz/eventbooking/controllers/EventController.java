package com.s4mz.eventbooking.controllers;

import com.s4mz.eventbooking.domain.CreateEventRequest;
import com.s4mz.eventbooking.domain.UpdateEventRequest;
import com.s4mz.eventbooking.domain.dtos.*;
import com.s4mz.eventbooking.domain.entities.Event;
import com.s4mz.eventbooking.mappers.EventMapper;
import com.s4mz.eventbooking.services.EventService;
import com.s4mz.eventbooking.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<GetEventResponseDto> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateEventRequestDto requestDto
    ){
        CreateEventRequest createEventRequest=eventMapper.fromDto(requestDto);
        Event event=eventService.createEvent(JwtUtil.parseUserId(jwt),createEventRequest);
        GetEventResponseDto eventResponseDto=eventMapper.toDto(event);
        return new ResponseEntity<>(eventResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ListEventResponseDto>> listEvents(
            @AuthenticationPrincipal Jwt jwt, Pageable pageable
            ){
        Page<Event> events=eventService.listEventsForOrganizer(JwtUtil.parseUserId(jwt),pageable);
        return ResponseEntity.ok(
                events.map(eventMapper::toListEventResponseDto)
        );
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<GetEventDetailsResponseDto> getEventDetails(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId
    ){
        return eventService.getEventForOrganizer(JwtUtil.parseUserId(jwt),eventId)
                .map(eventMapper::toGetEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }


    @PutMapping("/{eventId}")
    public ResponseEntity<UpdateEventResponseDto> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId,
            @Valid @RequestBody UpdateEventRequestDto requestDto
    ){
        UpdateEventRequest updateEventRequest=eventMapper.fromDto(requestDto);
        UUID userId=JwtUtil.parseUserId(jwt);
        Event updatedEvent=eventService.updateEventForOrganizer(userId,eventId,updateEventRequest);
        UpdateEventResponseDto updateEventResponseDto=eventMapper.toUpdateEventResponseDto(updatedEvent);
        return ResponseEntity.ok(updateEventResponseDto);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId
    ){
        eventService.deleteEventForOrganizer(JwtUtil.parseUserId(jwt),eventId);
        return ResponseEntity.noContent().build();
    }
}
