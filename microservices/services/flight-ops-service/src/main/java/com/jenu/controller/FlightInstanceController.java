package com.jenu.controller;

import com.jenu.payload.request.FlightInstanceRequest;
import com.jenu.payload.response.ApiResponse;
import com.jenu.payload.response.FlightInstanceResponse;
import com.jenu.service.FlightInstanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flight-instances")
public class FlightInstanceController {

    private final FlightInstanceService flightInstanceService;

    @PostMapping
    public ResponseEntity<FlightInstanceResponse> createFlightInstance(
           @Valid @RequestBody FlightInstanceRequest flightInstanceRequest,
            @RequestHeader("X-Airline-Id") Long airlineId
    ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flightInstanceService.createFlightInstance(airlineId,flightInstanceRequest));
    }


    @GetMapping("/{id}")
    public ResponseEntity<FlightInstanceResponse> getFlightInstanceById (
            @PathVariable Long id
    ) throws Exception {
        return ResponseEntity.ok(flightInstanceService.getFlightInstanceById(id));
    }

    @GetMapping
    public ResponseEntity<Page<FlightInstanceResponse>> getByAirlineId (
            @RequestHeader("X-Airline-Id") Long airlineId,
            @RequestParam(required = false)Long departureAirportId,
            @RequestParam(required = false)Long arrivalAirportId,
            @RequestParam(required = false) Long flightId,
            @RequestParam(required = false)LocalDate onDate,
            Pageable pageable
            ) throws Exception {
        return
                ResponseEntity.ok(flightInstanceService.getByAirlineId(
                airlineId,departureAirportId,arrivalAirportId,flightId,onDate,pageable
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightInstanceResponse> updateFlightInstance (
            @PathVariable Long id,
            @RequestBody FlightInstanceRequest flightInstanceRequest
    ) throws Exception {
        return ResponseEntity.ok(flightInstanceService.updateFlightInstance(id,flightInstanceRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteFlightInstance (
            @PathVariable Long id
    ) throws Exception {
        flightInstanceService.deleteFlightInstance(id);
        ApiResponse apiResponse = new ApiResponse("Flight instance deleted");
        return ResponseEntity.ok(apiResponse);
    }











}
