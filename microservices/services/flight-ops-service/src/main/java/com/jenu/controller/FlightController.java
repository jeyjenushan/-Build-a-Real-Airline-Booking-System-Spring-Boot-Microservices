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
            )  {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flightService.createFlight(airlineId,flightRequest));

    }

    /*
    * Create Flights it need to be interconnected by other services
    *     @PostMapping("/bulk")
    public ResponseEntity<List<FlightResponse>> createFlights(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody List<FlightRequest> requests) throws AirportException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flightService.createFlights(userId, requests));
    }
    *
    * */

    /*
    * GetFlightsById this method also we need for the interconnection so once interservice connection start to work on that
    *     @PostMapping("/batch")
    public ResponseEntity<Map<Long, FlightResponse>> getFlightsByIds(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(flightService.getFlightsByIds(ids));
    }
    * */

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlightById(
            @PathVariable Long id
    )  {
        return ResponseEntity.status(HttpStatus.OK)
                .body(flightService.getFlightById(id));

    }
    @GetMapping("/number/{flightNumber}")
    public ResponseEntity<FlightResponse> getFlightByNumber(
            @PathVariable String flightNumber)  {
        return ResponseEntity.ok(flightService.getFlightByNumber(flightNumber));
    }


    @GetMapping("/airline")
    public ResponseEntity<Page<FlightResponse>> getFlightsByAirline(
            @RequestHeader("Airline-Id") Long airlineId,
            @RequestParam(required = false) Long departureAirportId,
            @RequestParam(required = false)Long arrivalAirportId,
            Pageable pageable
    )  {
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
    )   {
        return ResponseEntity.ok(flightService.updateFlight(id,request));

    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FlightResponse> changeStatus(
            @PathVariable Long id,
            @RequestParam FlightStatus status
    )  {
        return ResponseEntity.ok(flightService.changeStatus(id,status));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(
            @RequestHeader("Airline-Id") Long airlineId,
            @PathVariable Long id
    )  {
       flightService.deleteFlight(airlineId,id);
       return ResponseEntity.noContent().build();

    }

}
