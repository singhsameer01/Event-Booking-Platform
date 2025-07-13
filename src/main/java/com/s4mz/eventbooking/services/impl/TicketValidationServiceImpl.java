package com.s4mz.eventbooking.services.impl;

import com.s4mz.eventbooking.domain.entities.*;
import com.s4mz.eventbooking.exceptions.QrCodeNotFoundException;
import com.s4mz.eventbooking.exceptions.TicketNotFoundException;
import com.s4mz.eventbooking.repositories.QrCodeRepository;
import com.s4mz.eventbooking.repositories.TicketRepository;
import com.s4mz.eventbooking.repositories.TicketValidationRepository;
import com.s4mz.eventbooking.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationServiceImpl implements TicketValidationService {

    private final QrCodeRepository qrCodeRepository;
    private final TicketValidationRepository ticketValidationRepository;
    private final TicketRepository ticketRepository;

    @Override
    public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
        QrCode qrCode=qrCodeRepository.findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE)
                .orElseThrow(()->new QrCodeNotFoundException(
                        "QR Code with ID "+qrCodeId+" was not found"
                ));
        Ticket ticket=qrCode.getTicket();

        return validateTicket(ticket);
    }

    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {
        Ticket ticket=ticketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);
        return validateTicket(ticket);
    }

    private TicketValidation validateTicket(Ticket ticket){

        TicketValidation ticketValidation=new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(TicketValidationMethod.MANUAL);
        TicketValidationStatusEnum ticketValidationStatusEnum=ticket.getValidations()
                .stream()
                .filter(v->TicketValidationStatusEnum.VALID.equals(v.getStatus()))
                .findFirst()
                .map(v->TicketValidationStatusEnum.INVALID)
                .orElse(TicketValidationStatusEnum.INVALID);
        return ticketValidationRepository.save(ticketValidation);
    }
}
