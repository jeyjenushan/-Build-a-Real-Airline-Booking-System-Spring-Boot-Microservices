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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flight-instances")
public class FlightInstanceController {

    private final FlightInstanceService flightInstanceService;

    @PostMapping
    public ResponseEntity<FlightInstanceResponse> createFlightInstance(
           @Valid @RequestBody FlightInstanceRequest flightInstanceRequest,
            @RequestHeader("X-User-Id") Long userId
    )  {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flightInstanceService.createFlightInstance(userId,flightInstanceRequest));
    }


    /*
    *     @PostMapping("/batch")
    public ResponseEntity<Map<Long, FlightInstanceResponse>> getFlightInstancesByIds(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(flightInstanceService.getFlightInstancesByIds(ids));
    }
    *
    * */



    @GetMapping("/{id}")
    public ResponseEntity<FlightInstanceResponse> getFlightInstanceById (
            @PathVariable Long id
    )  {
        return ResponseEntity.ok(flightInstanceService.getFlightInstanceById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<FlightInstanceResponse>> getFlightInstanceById()  {
        return ResponseEntity.ok(flightInstanceService.getFlightInstances());
    }

    @GetMapping
    public ResponseEntity<Page<FlightInstanceResponse>> getByAirlineId (
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false)Long departureAirportId,
            @RequestParam(required = false)Long arrivalAirportId,
            @RequestParam(required = false) Long flightId,
            @RequestParam(required = false)LocalDate onDate,
            Pageable pageable
            )  {
        return
                ResponseEntity.ok(flightInstanceService.getByAirlineId(
                userId,departureAirportId,arrivalAirportId,flightId,onDate,pageable
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightInstanceResponse> updateFlightInstance (
            @PathVariable Long id,
            @RequestBody FlightInstanceRequest flightInstanceRequest
    )  {
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
