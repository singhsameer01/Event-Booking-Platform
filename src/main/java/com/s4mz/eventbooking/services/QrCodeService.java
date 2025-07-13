package com.s4mz.eventbooking.services;

import com.s4mz.eventbooking.domain.entities.QrCode;
import com.s4mz.eventbooking.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);
    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);

}
