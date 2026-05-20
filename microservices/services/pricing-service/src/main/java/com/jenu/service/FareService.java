package com.jenu.service;

import com.jenu.model.Fare;
import com.jenu.payload.request.FareRequest;
import com.jenu.payload.response.FareResponse;

import java.util.List;
import java.util.Map;

public interface FareService {
    FareResponse createFare(FareRequest fareRequest) throws Exception;
    FareResponse getFareById(Long id) throws Exception;
    List<FareResponse> getFaresByFlightIdAndCabinClassId(
            Long flightId, Long cabinClassId
    );
    FareResponse updateFareById(Long id, FareRequest fareRequest) throws Exception;
    void deleteFareById(Long id) throws Exception;
    List<Fare> getFares();
    Map<Long,FareResponse> getLowestFarePerFlight(
            List<Long> flightIds,Long cabinClassId
    );

    Map<Long,FareResponse> getFaresByIds(
            List<Long> ids
    );


}
