package com.s4mz.eventbooking.controllers;

import com.s4mz.eventbooking.domain.dtos.GetEventDetailsResponseDto;
import com.s4mz.eventbooking.domain.dtos.GetPublishedEventDetailsResponseDto;
import com.s4mz.eventbooking.domain.dtos.ListPublishedEventResponseDto;
import com.s4mz.eventbooking.domain.entities.Event;
import com.s4mz.eventbooking.mappers.EventMapper;
import com.s4mz.eventbooking.services.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/published-events")
@RequiredArgsConstructor
@Slf4j
public class PublishedEventsController {
    private final EventService eventService;
    private final EventMapper eventMapper;
    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvents(
            @RequestParam(value = "q", required = false) String query,
            Pageable pageable
    ){
        Page<Event> events;
        if(StringUtils.hasText(query)){
            log.info("Calling searchEvents with query: {}", query);
            events=eventService.searchPublishedEvents(query,pageable);
        }
        else {
            events=eventService.listPublishedEvents(pageable);
        }
        return ResponseEntity.ok(
                events.map(eventMapper::toListPublishedEventResponseDto)
        );
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<GetPublishedEventDetailsResponseDto> getEventDetails(
            @PathVariable UUID eventId
    ){
        return eventService.getPublishedEvent(eventId)
                .map(eventMapper::toGetPublishedEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
