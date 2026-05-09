package com.jenu.service;

import com.jenu.payload.request.AirportRequest;
import com.jenu.payload.response.AirportResponse;

import java.util.List;

public interface AirportService {

    AirportResponse createAirport(AirportRequest airportRequest) throws Exception;
    AirportResponse getAirportById(Long id) throws Exception;
    List<AirportResponse> getAllAirports();
    AirportResponse updateAirport(Long id, AirportRequest airportRequest) throws Exception;
    void deleteAirport(Long id) throws Exception;
    List<AirportResponse> getAirportsByCityId(Long cityId);
}
