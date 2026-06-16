package com.jenu.service;

import com.jenu.enums.FlightStatus;
import com.jenu.exception.AirportException;
import com.jenu.payload.request.FlightRequest;
import com.jenu.payload.response.FlightResponse;
import com.jenu.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public interface FlightService {

    FlightResponse createFlight(Long userId, FlightRequest flightRequest);
    List<FlightResponse> createFlights(Long userId, List<FlightRequest> requests) ;
    FlightResponse getFlightById(Long flightId);
    FlightResponse getFlightByNumber(String flightNumber);
    Page<FlightResponse> getFlightsByAirline(Long airlineId,
                                             Long departureAirportId,
                                             Long arrivalAirportId,
                                          Pageable pageable
                                             );

    FlightResponse updateFlight(Long userId,FlightRequest flightRequest) ;
    void deleteFlight(Long userId,Long flightId);
    FlightResponse changeStatus(Long flightId, FlightStatus flightStatus) ;
    Map<Long, FlightResponse> getFlightsByIds(List<Long> ids);




}
