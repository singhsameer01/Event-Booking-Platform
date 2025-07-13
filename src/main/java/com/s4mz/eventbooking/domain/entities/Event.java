package com.s4mz.eventbooking.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @Column(name = "id",updatable = false,nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "name",nullable = false)
    String name;

    @Column(name = "start_date",columnDefinition = "TIMESTAMP")
    LocalDateTime start;

    @Column(name = "end_date",columnDefinition = "TIMESTAMP")
    LocalDateTime end;

    @Column(name = "venue",nullable = false)
    String venue;

    @Column(name = "sales_starts")
    LocalDateTime salesStart;

    @Column(name = "sales_ends")
    LocalDateTime salesEnd;

    @Column(name="status",nullable = false)
    @Enumerated(EnumType.STRING)
    EventStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    User organizer;

    @ManyToMany(mappedBy="attendingEvents")
    List<User> attendees=new ArrayList<>();

    @ManyToMany(mappedBy="staffingEvents")
    List<User> staff=new ArrayList<>();

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL,orphanRemoval = true)
    List<TicketType> ticketTypes=new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at",updatable = false, nullable = false)
    LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at",nullable = false)
    LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(start, event.start) && Objects.equals(end, event.end) && Objects.equals(venue, event.venue) && Objects.equals(salesStart, event.salesStart) && Objects.equals(salesEnd, event.salesEnd) && status == event.status && Objects.equals(createdAt, event.createdAt) && Objects.equals(updatedAt, event.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, start, end, venue, salesStart, salesEnd, status, createdAt, updatedAt);
    }
}
