package com.jenu.mapper;

import com.jenu.model.Flight;
import com.jenu.model.FlightSchedule;
import com.jenu.payload.request.FlightScheduleRequest;
import com.jenu.payload.response.AirportResponse;
import com.jenu.payload.response.FlightScheduleResponse;

public class FlightScheduleMapper {
    public static FlightSchedule convertToFlightSchedule(
            FlightScheduleRequest flightScheduleRequest,
            Flight flight){
        if(flightScheduleRequest == null || flight == null) return null;
        return FlightSchedule.builder()
                .arrivalAirportId(flight.getArrivalAirportId())
                .departureAirportId(flight.getDepartureAirportId())
                .arrivalTime(flightScheduleRequest.getArrivalTime())
                .departureTime(flightScheduleRequest.getDepartureTime())
                .startDate(flightScheduleRequest.getStartDate())
                .endDate(flightScheduleRequest.getEndDate())
                .operatingDays(flightScheduleRequest.getOperatingDays())
                .isActive(flightScheduleRequest.getIsActive())
                .build();
    }

    public static FlightScheduleResponse convertToFlightScheduleResponse(
            FlightSchedule flightSchedule,
            AirportResponse arrivalAirportResponse,
            AirportResponse departureAirportResponse
    ){
        if(flightSchedule==null) return null;
        return FlightScheduleResponse.builder()
                .id(flightSchedule.getId())
                .flightId(flightSchedule.getFlight() != null ? flightSchedule.getFlight().getId() : null)
                .flightNumber(flightSchedule.getFlight() != null ? flightSchedule.getFlight().getFlightNumber() : null)
                .departureAirport(departureAirportResponse)
                .arrivalAirport(arrivalAirportResponse)
                .departureTime(flightSchedule.getDepartureTime())
                .arrivalTime(flightSchedule.getArrivalTime())
                .startDate(flightSchedule.getStartDate())
                .endDate(flightSchedule.getEndDate())
                .isActive(flightSchedule.getIsActive())
                .operatingDays(flightSchedule.getOperatingDays())
                .build();
    }

    public static void updateEntity(
            FlightScheduleRequest request,FlightSchedule existing
    ){
        if(existing==null || request==null) return;
        if(request.getDepartureTime()!=null) existing.setDepartureTime(request.getDepartureTime());
        if(request.getArrivalTime()!=null) existing.setArrivalTime(request.getArrivalTime());
        if(request.getOperatingDays()!=null) existing.setOperatingDays(request.getOperatingDays());
        if(request.getIsActive()!=null) existing.setIsActive(request.getIsActive());
        if(request.getStartDate()!=null) existing.setStartDate(request.getStartDate());
        if(request.getEndDate()!=null) existing.setEndDate(request.getEndDate());

    }


}
