package com.jenu.service;
import com.jenu.payload.request.FlightInstanceRequest;
import com.jenu.payload.response.FlightInstanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface FlightInstanceService {

    FlightInstanceResponse createFlightInstance(
            Long airlineId,
            FlightInstanceRequest request
    ) throws Exception;
    FlightInstanceResponse getFlightInstanceById(Long id) throws Exception;
    Page<FlightInstanceResponse> getByAirlineId(Long airlineId,Long departureAirportId,
                                                Long arrivalAirportId,Long flightId,
                                                LocalDate onDate,
                                                Pageable pageable);
    FlightInstanceResponse updateFlightInstance(
            Long id,FlightInstanceRequest flightInstanceRequest
    ) throws Exception;
    void deleteFlightInstance(Long id) throws Exception;


}
