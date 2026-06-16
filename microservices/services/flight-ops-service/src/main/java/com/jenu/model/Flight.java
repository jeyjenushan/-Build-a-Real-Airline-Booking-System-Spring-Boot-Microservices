package com.jenu.model;


import com.jenu.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flights")
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {})
@EqualsAndHashCode(of = "id")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String flightNumber;

    // Cross-service ref: Airline is in airline-core-service
    @Column(name = "airline_id", nullable = false)
    private Long airlineId;

    // Cross-service ref: Aircraft is in airline-core-service
    @Column(name = "aircraft_id", nullable = false)
    private Long aircraftId;

    // Cross-service ref: Airport is in location-service
    @Column(name = "departure_airport_id", nullable = false)
    private Long departureAirportId;

    // Cross-service ref: Airport is in location-service
    @Column(name = "arrival_airport_id", nullable = false)
    private Long arrivalAirportId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FlightStatus status=FlightStatus.SCHEDULED;

    @CreatedDate
    @Column(updatable = false,nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;




}
