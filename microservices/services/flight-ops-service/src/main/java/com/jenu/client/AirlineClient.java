package com.jenu.client;

import com.jenu.payload.response.AircraftResponse;
import com.jenu.payload.response.AirlineResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "airline-core-service")
public interface AirlineClient {

    @GetMapping("/api/airlines/{id}")
     AirlineResponse getAirlineById(@PathVariable Long id);

    @GetMapping("/api/aircrafts/{id}")
    AircraftResponse getAircraft(
            @PathVariable Long id
    );
    @GetMapping("/api/airlines/admin")
    AirlineResponse getAirlineByOwner
            (@RequestHeader("X-User-id")Long userId);
}

