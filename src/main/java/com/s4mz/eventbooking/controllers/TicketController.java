package com.s4mz.eventbooking.controllers;


import com.s4mz.eventbooking.domain.dtos.GetTicketResponseDto;
import com.s4mz.eventbooking.domain.dtos.ListTicketResponseDto;
import com.s4mz.eventbooking.domain.entities.QrCode;
import com.s4mz.eventbooking.domain.entities.Ticket;
import com.s4mz.eventbooking.mappers.TicketMapper;
import com.s4mz.eventbooking.services.QrCodeService;
import com.s4mz.eventbooking.services.TicketService;
import com.s4mz.eventbooking.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final QrCodeService qrCodeService;

    @GetMapping
    public Page<ListTicketResponseDto> listTicketsForUser(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable
    ){
        return ticketService.listTicketsForUser(
                JwtUtil.parseUserId(jwt),
                pageable
        ).map(ticketMapper::toListTicketResponseDto);
    }

    @GetMapping(path = "/{ticket-id}")
    public ResponseEntity<GetTicketResponseDto> getTicket(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("ticket-id") UUID ticketId
    ){
        return ticketService
                .getTicketForUser(JwtUtil.parseUserId(jwt),ticketId)
                .map(ticketMapper::toGetTicketResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/{ticketId}/qr-codes")
    public ResponseEntity<byte[]> getTicketQrCode(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("ticketId") UUID ticketId
    ){
        byte[] qrCodeImage= qrCodeService.getQrCodeImageForUserAndTicket(JwtUtil.parseUserId(jwt),ticketId);
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_PNG);
        httpHeaders.setContentLength(qrCodeImage.length);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(qrCodeImage);
    }

}
