package com.jenu.service;

import com.jenu.payload.request.AircraftRequest;
import com.jenu.payload.response.AircraftResponse;

import java.util.List;

public interface AircraftService {
    AircraftResponse createAircraft(AircraftRequest aircraftRequest,Long ownerId) throws Exception;
    AircraftResponse getAircraft(Long id) throws Exception;
    List<AircraftResponse> listAllAircraftByOwnerId(Long ownerId) throws Exception;
    AircraftResponse updateAircraft(Long id,AircraftRequest aircraftRequest,Long ownerId) throws Exception;
    void deleteAircraft(Long id,Long ownerId) throws Exception;

}
