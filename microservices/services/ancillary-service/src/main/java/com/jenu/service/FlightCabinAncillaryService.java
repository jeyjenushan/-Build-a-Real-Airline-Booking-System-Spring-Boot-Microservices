package com.jenu.service;

import com.jenu.enums.AncillaryType;
import com.jenu.payload.request.FlightCabinAncillaryRequest;
import com.jenu.payload.response.FlightCabinAncillaryResponse;


import java.util.List;

public interface FlightCabinAncillaryService {

    FlightCabinAncillaryResponse create(FlightCabinAncillaryRequest request) throws Exception;

    List<FlightCabinAncillaryResponse> bulkCreate(List<FlightCabinAncillaryRequest> requests) throws Exception;

    FlightCabinAncillaryResponse getById(Long id) throws Exception;

    List<FlightCabinAncillaryResponse> getAllByFlightAndCabinClass(
            Long flightId, Long cabinClassId);

    List<FlightCabinAncillaryResponse> getAllByIds(List<Long> ids);
    FlightCabinAncillaryResponse getByFlightIdAndCabinClassAndType(
            Long flightId, Long cabinClassId, AncillaryType type) throws Exception;

    List<FlightCabinAncillaryResponse> getAllByFlightIdAndCabinClassAndType(
            Long flightId, Long cabinClassId, AncillaryType type) throws Exception;

    FlightCabinAncillaryResponse update(Long id, FlightCabinAncillaryRequest request) throws Exception;

    void delete(Long id);

    Double calculateAncillaryPrice(List<Long> ancillaryIds);
}
