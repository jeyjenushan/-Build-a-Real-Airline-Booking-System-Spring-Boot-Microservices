package com.jenu.service;

import com.jenu.payload.request.FlightScheduleRequest;
import com.jenu.payload.response.FlightScheduleResponse;

import java.util.List;

public interface FlightScheduleService {

    FlightScheduleResponse createFlightSchedule(
            Long userId,
            FlightScheduleRequest flightScheduleRequest) throws Exception;
    FlightScheduleResponse getFlightSchedule(Long flightScheduleId) throws Exception;
    List<FlightScheduleResponse> getAllFlightSchedulesByAirline(Long userId);
    void deleteFlightSchedule(Long flightScheduleId) throws Exception;
    FlightScheduleResponse updateFlightSchedule(Long id, FlightScheduleRequest flightScheduleRequest) throws Exception;
}
