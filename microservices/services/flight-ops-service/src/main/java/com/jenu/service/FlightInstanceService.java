package com.jenu.service;
import com.jenu.payload.request.FlightInstanceRequest;
import com.jenu.payload.response.FlightInstanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FlightInstanceService {

    FlightInstanceResponse createFlightInstance(
            Long airlineId,
            FlightInstanceRequest request
    ) ;
    List<FlightInstanceResponse> getFlightInstances();
    FlightInstanceResponse getFlightInstanceById(Long id) ;
    Page<FlightInstanceResponse> getByAirlineId(Long airlineId,Long departureAirportId,
                                                Long arrivalAirportId,Long flightId,
                                                LocalDate onDate,
                                                Pageable pageable);
    FlightInstanceResponse updateFlightInstance(
            Long id,FlightInstanceRequest flightInstanceRequest
    ) ;
    void deleteFlightInstance(Long id) ;
    Map<Long, FlightInstanceResponse> getFlightInstancesByIds(List<Long> ids);


}
