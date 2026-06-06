package com.jenu.service;

import com.jenu.exception.AirportException;
import com.jenu.exception.CityException;
import com.jenu.payload.request.AirportRequest;
import com.jenu.payload.response.AirportResponse;

import java.util.List;

public interface AirportService {

    AirportResponse createAirport(AirportRequest airportRequest) throws AirportException, CityException;
    List<AirportResponse> createBulkAirports(List<AirportRequest> requests) throws AirportException, CityException;
    AirportResponse getAirportById(Long id) throws Exception;
    List<AirportResponse> getAllAirports();
    AirportResponse updateAirport(Long id, AirportRequest airportRequest) throws AirportException, CityException;
    void deleteAirport(Long id) throws AirportException;
    List<AirportResponse> getAirportsByCityId(Long cityId);
}
