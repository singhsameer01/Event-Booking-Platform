package com.s4mz.eventbooking.domain.dtos;

import com.s4mz.eventbooking.domain.entities.EventStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListPublishedEventResponseDto {
    UUID id;
    String name;
    LocalDateTime start;
    LocalDateTime end;
    String venue;
}
