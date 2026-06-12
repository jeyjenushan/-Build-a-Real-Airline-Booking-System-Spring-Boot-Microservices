package com.jenu.service;

import com.jenu.exception.ResourceNotFoundException;
import com.jenu.payload.request.AircraftRequest;
import com.jenu.payload.response.AircraftResponse;

import java.util.List;

public interface AircraftService {
    AircraftResponse createAircraft(AircraftRequest aircraftRequest,Long ownerId) ;
    AircraftResponse getAircraft(Long id) throws ResourceNotFoundException;
    List<AircraftResponse> listAllAircraftByOwnerId(Long ownerId);
    AircraftResponse updateAircraft(Long id,AircraftRequest aircraftRequest,Long ownerId) throws ResourceNotFoundException;
    void deleteAircraft(Long id,Long ownerId) throws ResourceNotFoundException;

}
