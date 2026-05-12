package com.jenu.controller;


import com.jenu.payload.request.AircraftRequest;
import com.jenu.payload.response.AircraftResponse;
import com.jenu.service.AircraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aircrafts")
public class AircraftController {
    private final AircraftService aircraftService;

    @PostMapping
    public ResponseEntity<AircraftResponse> createAircraft(
         @Valid @RequestBody AircraftRequest aircraftRequest,
         @RequestHeader("X-User-Id") Long userId
         ) throws Exception {
        AircraftResponse aircraftResponse = aircraftService.createAircraft(aircraftRequest,userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(aircraftResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AircraftResponse> getAircraft(
            @PathVariable Long id
    ) throws Exception {
        return ResponseEntity.ok(aircraftService.getAircraft(id));
    }

    @GetMapping
    public ResponseEntity<List<AircraftResponse>> getAllAircrafts(
            @RequestHeader("X-User-Id") Long userId
    ) throws Exception {
        return ResponseEntity.ok(aircraftService.listAllAircraftByOwnerId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AircraftResponse> updateAircraft(
            @PathVariable Long id,
            @RequestBody AircraftRequest aircraftRequest,
            @RequestHeader("X-User-Id") Long userId
    ) throws Exception {
        return ResponseEntity.ok(aircraftService.updateAircraft(id,aircraftRequest,userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AircraftResponse> deleteAircraft(
            @PathVariable Long id,     @RequestHeader("X-User-Id") Long userId
    ) throws Exception {
        aircraftService.deleteAircraft(id,userId);
        return ResponseEntity.noContent().build();
    }






}
