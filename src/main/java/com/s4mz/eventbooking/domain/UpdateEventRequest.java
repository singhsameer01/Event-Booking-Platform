package com.s4mz.eventbooking.domain;


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
public class UpdateEventRequest {

    String name;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String venue;
    LocalDateTime salesStart;
    LocalDateTime salesEnd;
    EventStatusEnum status;
    List<UpdateTicketTypeRequest> ticketTypes=new ArrayList<>();
}
