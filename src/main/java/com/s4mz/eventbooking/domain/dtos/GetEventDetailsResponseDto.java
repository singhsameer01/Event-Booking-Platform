package com.s4mz.eventbooking.domain.dtos;

import com.s4mz.eventbooking.domain.entities.EventStatusEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetEventDetailsResponseDto {
    UUID id;
    String name;
    LocalDateTime start;
    LocalDateTime end;
    String venue;
    LocalDateTime salesStart;
    LocalDateTime salesEnd;
    EventStatusEnum status;
    List<GetEventDetailsTicketTypesResponseDto> ticketTypes = new ArrayList<>();
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
