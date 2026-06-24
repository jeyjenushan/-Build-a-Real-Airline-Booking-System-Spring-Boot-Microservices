package com.jenu.controller;
import com.jenu.payload.request.FlightScheduleRequest;
import com.jenu.payload.response.ApiResponse;
import com.jenu.payload.response.FlightScheduleResponse;
import com.jenu.service.FlightScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight-schedules")
@RequiredArgsConstructor
public class FlightScheduleController {

    private final FlightScheduleService flightScheduleService;

    @PostMapping
    public ResponseEntity<FlightScheduleResponse>
    createFlightSchedule(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody FlightScheduleRequest flightScheduleRequest) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flightScheduleService.createFlightSchedule(userId,flightScheduleRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightScheduleResponse> getFlightScheduleById (
            @PathVariable Long id
    )  {
        return ResponseEntity.ok(flightScheduleService.getFlightSchedule(id));
    }

    @GetMapping
    public ResponseEntity<List<FlightScheduleResponse>> getFlightSchedules (
            @RequestHeader("X-UserId-Id") Long userId
    )  {
        return ResponseEntity.ok(flightScheduleService.getAllFlightSchedulesByAirline(userId));
    }



    @PutMapping("/{id}")
    public ResponseEntity<FlightScheduleResponse> updateFlightInstance (
            @PathVariable Long id,
            @RequestBody FlightScheduleRequest flightScheduleRequest
    )  {
        return ResponseEntity.ok(flightScheduleService.updateFlightSchedule(id,flightScheduleRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteFlightInstance (
            @PathVariable Long id
    )  {
        flightScheduleService.deleteFlightSchedule(id);
        ApiResponse apiResponse = new ApiResponse("Flight instance deleted");
        return ResponseEntity.ok(apiResponse);
    }

}
