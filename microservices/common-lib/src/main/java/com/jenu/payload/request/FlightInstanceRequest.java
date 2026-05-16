package com.jenu.payload.request;

import com.jenu.enums.FlightStatus;
import com.jenu.payload.response.AirportResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightInstanceRequest {
    @NotNull(message = "Flight ID is required")
    private Long flightId;
    private Long scheduleId;
    private Long departureAirportId;
    private Long arrivalAirportId;
    @NotNull(message = "Departure date-time is required")
    private LocalDateTime departureDateTime;
    @NotNull(message = "Arrival date-time is required")
    private LocalDateTime arrivalDateTime;
    @NotNull(message = "Total seats is required")
    @Positive
    private int totalSeats;
    @PositiveOrZero
    private int availableSeats;
    private int minAdvanceBookingDays;
    private int maxAdvanceBookingDays;
    private Boolean isActive;
    private FlightStatus flightStatus;
}
