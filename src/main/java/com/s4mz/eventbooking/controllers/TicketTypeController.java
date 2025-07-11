package com.s4mz.eventbooking.controllers;


import com.s4mz.eventbooking.services.TicketTypeService;
import com.s4mz.eventbooking.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events/{event-id}/ticket-types")
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    @PostMapping("/{ticket-type-id}/tickets")
    public ResponseEntity<Void> purchaseTicket(
            @PathVariable("event-id") UUID eventId,
            @PathVariable("ticket-type-id") UUID ticketTypeId,
            @AuthenticationPrincipal Jwt jwt) {

        ticketTypeService.purchaseTicket(JwtUtil.parseUserId(jwt), ticketTypeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
