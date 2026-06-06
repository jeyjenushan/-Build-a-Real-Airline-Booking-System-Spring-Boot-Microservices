package com.jenu.mapper;

import com.jenu.model.Airport;
import com.jenu.model.City;
import com.jenu.payload.request.AirportRequest;
import com.jenu.payload.request.CityRequest;
import com.jenu.payload.response.AirportResponse;
import com.jenu.payload.response.CityResponse;

public class AirportMapper {


    public static Airport ConvertToAirport(AirportRequest airportRequest) {
        if(airportRequest == null) return null;
        return Airport.builder()
                .address(airportRequest.getAddress())
                .geoCode(airportRequest.getGeoCode())
                .timeZoneId(airportRequest.getTimezone() != null ? airportRequest.getTimezone().getId() : null)
                .iataCode(airportRequest.getIataCode())
                .name(airportRequest.getAirportName())
                .build();
    }

    public static AirportResponse ConvertToAirportResponse(Airport airport) {
        if(airport == null) return null;
        return AirportResponse.builder()
                .iataCode(airport.getIataCode())
                .cityResponse(CityMapper.ConvertToCityResponse(airport.getCity()))
                .address(airport.getAddress())
                .analytics(airport.getAnalytics())
                .detailedName(airport.getDetailedName())
                .timeZone(airport.getTimeZone())
                .geoCode(airport.getGeoCode())
                .name(airport.getName())
                .id(airport.getId())
                .build();
    }
    public static Airport updateEntity(AirportRequest request,Airport existingAirport){
        if(request == null || existingAirport == null) return null;

        if(request.getIataCode() !=null){
            existingAirport.setIataCode(request.getIataCode());
        }
        if(request.getAirportName()!=null){
            existingAirport.setName(request.getAirportName());
        }
        if(request.getAddress() !=null){
            existingAirport.setAddress(request.getAddress());
        }
        if(request.getTimezone() !=null){
            existingAirport.setTimeZone(request.getTimezone());
        }
        if(request.getAddress() !=null){
            existingAirport.setAddress(request.getAddress());
        }
        if(request.getGeoCode() !=null){
            existingAirport.setGeoCode(request.getGeoCode());
        }
        return existingAirport;
    }


}
