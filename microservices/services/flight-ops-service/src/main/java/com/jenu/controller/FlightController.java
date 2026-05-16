package com.jenu.controller;

import com.jenu.enums.FlightStatus;
import com.jenu.payload.request.FlightRequest;
import com.jenu.payload.response.FlightResponse;
import com.jenu.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightController {
    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(
            @Valid @RequestBody FlightRequest flightRequest,
            @RequestHeader("Airline-Id") Long airlineId
            ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flightService.createFlight(airlineId,flightRequest));

    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlightById(
            @PathVariable Long id
    ) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(flightService.getFlightById(id));

    }

    @GetMapping("/airline")
    public ResponseEntity<Page<FlightResponse>> getFlightsByAirline(
            @RequestHeader("Airline-Id") Long airlineId,
            @RequestParam(required = false) Long departureAirportId,
            @RequestParam(required = false)Long arrivalAirportId,
            Pageable pageable
    ) throws Exception {
        return ResponseEntity.ok(
                flightService.getFlightsByAirline(
                        airlineId,departureAirportId,arrivalAirportId,pageable
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightResponse> updateFlight(
            @PathVariable Long id,
            @RequestBody FlightRequest request
    ) throws Exception {
        return ResponseEntity.ok(flightService.updateFlight(id,request));

    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FlightResponse> changeStatus(
            @PathVariable Long id,
            @RequestParam FlightStatus status
    ) throws Exception {
        return ResponseEntity.ok(flightService.changeStatus(id,status));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(
            @RequestHeader("Airline-Id") Long airlineId,
            @PathVariable Long id
    ) throws Exception {
       flightService.deleteFlight(airlineId,id);
       return ResponseEntity.noContent().build();

    }

}
