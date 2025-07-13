package com.s4mz.eventbooking.domain.dtos;

import com.s4mz.eventbooking.domain.entities.TicketValidationMethod;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketValidationRequestDto {
    UUID id;
    TicketValidationMethod method;
}
