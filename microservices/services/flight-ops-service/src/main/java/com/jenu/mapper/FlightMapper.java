package com.jenu.mapper;

import com.jenu.model.Flight;
import com.jenu.payload.request.FlightRequest;
import com.jenu.payload.response.AircraftResponse;
import com.jenu.payload.response.AirlineResponse;
import com.jenu.payload.response.AirportResponse;
import com.jenu.payload.response.FlightResponse;

public class FlightMapper {

    public static Flight convertToFlightEntity(FlightRequest flightRequest){
        if(flightRequest == null) return null;
        return Flight.builder()
                .flightNumber(flightRequest.getFlightNumber())
                .aircraftId(flightRequest.getAircraftId())
                .departureAirportId(flightRequest.getDepartureAirportId())
                .arrivalAirportId(flightRequest.getArrivalAirportId())
                .build();
    }

    public static FlightResponse convertToFlightResponse(Flight flight, AircraftResponse aircraftResponse, AirlineResponse airlineResponse,
                                                         AirportResponse departureAirport, AirportResponse arrivalAirport){
        if(flight == null) return null;
        return FlightResponse
                .builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .airline(airlineResponse)
                .aircraft(aircraftResponse)
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .status(flight.getStatus())
                .createdAt(flight.getCreatedAt())
                .updatedAt(flight.getUpdatedAt())
                .build();
    }

    public static void updateEntity(Flight flight, FlightRequest flightRequest){
        if(flight == null || flightRequest==null) return;
        if(flightRequest.getFlightNumber()!=null) flight.setFlightNumber(flightRequest.getFlightNumber());
        if(flightRequest.getAircraftId()!=null) flight.setAircraftId(flightRequest.getAircraftId());
        if(flightRequest.getDepartureAirportId()!=null)flight.setDepartureAirportId(flightRequest.getDepartureAirportId());
        if(flightRequest.getArrivalAirportId()!=null)flight.setArrivalAirportId(flightRequest.getArrivalAirportId());
        if(flightRequest.getStatus()!=null) flight.setStatus(flightRequest.getStatus());

    }
}
