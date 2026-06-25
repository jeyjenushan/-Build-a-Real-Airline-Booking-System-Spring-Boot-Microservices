package com.jenu.payload.response;

import com.jenu.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightInstanceResponse {
    private Long id;
    private Long flightId;
    private String flightNumber;
    private Long airlineId;
    private String airlineName;
    private String airlineLogo;
    private String aircraftId;
    private String aircraftModal;
    private String aircraftCode;
    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;
    private FareResponse fare;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private String formattedDuration;
    private int totalSeats;
    private FlightStatus flightStatus;
    private int availableSeats;
    private int minAdvanceBookingDays;
    private int maxAdvanceBookingDays;
    private Boolean isActive;

}
