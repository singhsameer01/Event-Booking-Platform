package com.s4mz.eventbooking.mappers;

import com.s4mz.eventbooking.domain.CreateEventRequest;
import com.s4mz.eventbooking.domain.CreateTicketTypeRequest;
import com.s4mz.eventbooking.domain.UpdateEventRequest;
import com.s4mz.eventbooking.domain.UpdateTicketTypeRequest;
import com.s4mz.eventbooking.domain.dtos.*;
import com.s4mz.eventbooking.domain.entities.Event;
import com.s4mz.eventbooking.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);
    CreateEventRequest fromDto(CreateEventRequestDto dto);

    GetEventResponseDto toDto(Event event);

    ListEventTicketTypeResponseDto toDto(TicketType ticketType);

    ListEventResponseDto toListEventResponseDto(Event event);

    GetEventDetailsTicketTypesResponseDto toGetEventDetailsTicketTypesResponseDto(TicketType ticketType);

    GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);

    UpdateTicketTypeRequest fromDto(UpdateTicketTypeRequestDto dto);

    UpdateEventRequest fromDto(UpdateEventRequestDto dto);

    UpdateTicketTypeResponseDto toUpdateTicketTypeResponseDto(TicketType ticketType);

    UpdateEventResponseDto toUpdateEventResponseDto(Event event);

    ListPublishedEventResponseDto toListPublishedEventResponseDto(Event event);

    GetPublishedEventDetailsTicketTypesResponseDto toGetPublishedEventDetailsTicketTypesResponseDto(TicketType ticketType);

    GetPublishedEventDetailsResponseDto toGetPublishedEventDetailsResponseDto(Event event);
}
