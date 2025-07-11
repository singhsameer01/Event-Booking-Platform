package com.s4mz.eventbooking.domain.dtos;

import com.s4mz.eventbooking.domain.entities.EventStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateEventRequestDto {
    @NotBlank(message = "Name is required")
    String name;
    LocalDateTime startDate;
    LocalDateTime endDate;
    @NotBlank(message = "Venue details are required")
    String venue;
    LocalDateTime salesStart;
    LocalDateTime salesEnd;

    @NotNull(message = "Event status is required")
    EventStatusEnum status;
    @NotNull(message = "At least one ticket type is required")
    @Valid
    List<CreateTicketTypeRequestDto> ticketTypes=new ArrayList<>();
}
