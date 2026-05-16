package com.jenu.service;

import com.jenu.enums.FlightStatus;
import com.jenu.payload.request.FlightRequest;
import com.jenu.payload.response.FlightResponse;
import com.jenu.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public interface FlightService {

    FlightResponse createFlight(Long airlineId, FlightRequest flightRequest) throws Exception;
    Page<FlightResponse> getFlightsByAirline(Long airlineId,
                                             Long departureAirportId,
                                             Long arrivalAirportId,
                                          Pageable pageable
                                             );
    FlightResponse getFlightById(Long flightId);
    FlightResponse updateFlight(Long airlineId,FlightRequest flightRequest) throws Exception;
    void deleteFlight(Long airlineId,Long flightId);
    FlightResponse changeStatus(Long flightId, FlightStatus flightStatus);




}
