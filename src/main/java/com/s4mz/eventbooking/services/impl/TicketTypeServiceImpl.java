package com.s4mz.eventbooking.services.impl;

import com.s4mz.eventbooking.domain.entities.Ticket;
import com.s4mz.eventbooking.domain.entities.TicketStatusEnum;
import com.s4mz.eventbooking.domain.entities.TicketType;
import com.s4mz.eventbooking.domain.entities.User;
import com.s4mz.eventbooking.exceptions.TicketSoldOutException;
import com.s4mz.eventbooking.exceptions.TicketTypeNotFoundException;
import com.s4mz.eventbooking.exceptions.UserNotFoundException;
import com.s4mz.eventbooking.repositories.TicketRepository;
import com.s4mz.eventbooking.repositories.TicketTypeRepository;
import com.s4mz.eventbooking.repositories.UserRepository;
import com.s4mz.eventbooking.services.QrCodeService;
import com.s4mz.eventbooking.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final QrCodeService qrCodeService;

    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
        User user=userRepository.findById(userId).orElseThrow(
                ()-> new UserNotFoundException("User with id: "+userId+" not found")
        );
        TicketType ticketType= ticketTypeRepository.findByIdWithLock(ticketTypeId).orElseThrow(
                ()-> new TicketTypeNotFoundException("Ticket type with id: "+ticketTypeId+" not found")
        );
        int purchasedTickets=ticketRepository.countByTicketTypeId(ticketType.getId());
        if(purchasedTickets +1> ticketType.getTicketsAvailable()) {
            throw new TicketSoldOutException("Ticket type with id: "+ticketTypeId+" is sold out");
        }

        Ticket ticket=new Ticket();
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(user);

        Ticket savedTicket = ticketRepository.saveAndFlush(ticket);

        qrCodeService.generateQrCode(savedTicket);

        ticketType.setTicketsAvailable(ticketType.getTicketsAvailable() - 1);
        ticketTypeRepository.save(ticketType);

        return savedTicket;

    }
}
