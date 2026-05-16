package com.jenu.mapper;

import com.jenu.enums.FlightStatus;
import com.jenu.model.Flight;
import com.jenu.model.FlightInstance;
import com.jenu.payload.request.FlightInstanceRequest;
import com.jenu.payload.response.AircraftResponse;
import com.jenu.payload.response.AirlineResponse;
import com.jenu.payload.response.AirportResponse;
import com.jenu.payload.response.FlightInstanceResponse;

public class FlightInstanceMapper {

    public static FlightInstance convertToFlightInstanceEntity
            (FlightInstanceRequest flightInstanceRequest, Flight flight){
        if(flightInstanceRequest == null || flight == null) return null;

        return FlightInstance.builder()
                .flight(flight)
                .airlineId(flight.getAirlineId())
                .scheduleId(flightInstanceRequest.getScheduleId())
                .departureAirportId(flightInstanceRequest.getDepartureAirportId())
                .arrivalAirportId(flightInstanceRequest.getArrivalAirportId())
                .arrivalDateTime(flightInstanceRequest.getArrivalDateTime())
                .departureDateTime(flightInstanceRequest.getDepartureDateTime())
                .maximumAdvancedBookingDays(flightInstanceRequest.getMaxAdvanceBookingDays())
                .minimumAdvancedBookingDays(flightInstanceRequest.getMinAdvanceBookingDays())
                .status(FlightStatus.SCHEDULED)
                .isActive(flightInstanceRequest.getIsActive())
                .build();

    }

    public static FlightInstanceResponse convertToFlightInstanceResponse
            (FlightInstance flightInstance,
             AircraftResponse aircraftResponse,
             AirlineResponse airlineResponse,
             AirportResponse departureAirportResponse,
             AirportResponse arrivalAirportResponse
             ){
        if(flightInstance == null ) return null;
        return FlightInstanceResponse.builder()
                .id(flightInstance.getId())
                .flightId(flightInstance.getFlight().getId())
                .flightNumber(flightInstance.getFlight().getFlightNumber())
                .aircraftId(String.valueOf(flightInstance.getFlight().getAircraftId()))
                .aircraftModal(aircraftResponse.getModel())
                .aircraftCode(aircraftResponse.getCode())
                .airlineId(flightInstance.getAirlineId())
                .airlineName(airlineResponse.getName())
                .airlineLogo(airlineResponse.getLogoUrl())
                .departureAirport(departureAirportResponse)
                .arrivalAirport(arrivalAirportResponse)
                .departureDateTime(flightInstance.getDepartureDateTime())
                .arrivalDateTime(flightInstance.getArrivalDateTime())
                .formattedDuration(flightInstance.getFormatedDuration())
                .totalSeats(flightInstance.getTotalSeats())
                .availableSeats(flightInstance.getAvailableSeats())
                .flightStatus(flightInstance.getStatus())
                .isActive(flightInstance.getIsActive())
                .minAdvanceBookingDays(flightInstance.getMinimumAdvancedBookingDays())
                .maxAdvanceBookingDays(flightInstance.getMaximumAdvancedBookingDays())
                .build();


    }

    public static void updateEntity(FlightInstanceRequest flightInstanceRequest, FlightInstance existingFlightInstance){
        if(flightInstanceRequest == null || existingFlightInstance == null) return;
        if(flightInstanceRequest.getDepartureAirportId() != null) existingFlightInstance.setDepartureAirportId(flightInstanceRequest.getDepartureAirportId());
        if(flightInstanceRequest.getArrivalAirportId()!= null)existingFlightInstance.setArrivalAirportId(flightInstanceRequest.getArrivalAirportId());
        if(flightInstanceRequest.getDepartureDateTime()!= null)existingFlightInstance.setDepartureDateTime(flightInstanceRequest.getDepartureDateTime());
        if(flightInstanceRequest.getArrivalDateTime()!= null)existingFlightInstance.setArrivalDateTime(flightInstanceRequest.getArrivalDateTime());
        if(flightInstanceRequest.getFlightStatus()!=null)existingFlightInstance.setStatus(flightInstanceRequest.getFlightStatus());
        existingFlightInstance.setAvailableSeats(flightInstanceRequest.getAvailableSeats());
        existingFlightInstance.setIsActive(flightInstanceRequest.getIsActive());
        existingFlightInstance.setMinimumAdvancedBookingDays(flightInstanceRequest.getMinAdvanceBookingDays());
        existingFlightInstance.setMaximumAdvancedBookingDays(flightInstanceRequest.getMaxAdvanceBookingDays());
    }






}

