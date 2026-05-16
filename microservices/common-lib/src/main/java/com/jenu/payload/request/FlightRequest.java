package com.jenu.payload.request;

import com.jenu.enums.FlightStatus;
import com.jenu.payload.response.AircraftResponse;
import com.jenu.payload.response.AirlineResponse;
import com.jenu.payload.response.AirportResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlightRequest {
    @NotBlank(message = "Flight number is required")
    @Size(max = 10)
    private String flightNumber;

    private Long airlineId;

    @NotBlank(message = "Aircraft ID is required")
    private Long aircraftId;

    @NotBlank(message = "Departure airport ID is required")
    private Long departureAirportId;

    @NotBlank(message = "Arrival airport ID is required")
    private Long arrivalAirportId;

    private FlightStatus status;


}
