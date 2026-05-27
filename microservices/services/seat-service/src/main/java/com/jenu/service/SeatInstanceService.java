package com.jenu.service;

import com.jenu.enums.SeatAvailabilityStatus;
import com.jenu.payload.request.SeatInstanceRequest;
import com.jenu.payload.response.SeatInstanceResponse;


import java.util.List;

public interface SeatInstanceService {

    SeatInstanceResponse createSeatInstance(SeatInstanceRequest request);
    SeatInstanceResponse getSeatInstanceById(Long id);
    List<SeatInstanceResponse> getSeatInstancesByFlightId(Long flightId);
    List<SeatInstanceResponse> getAvailableSeatsByFlightId(Long flightId);
    List<SeatInstanceResponse> getAllByIds(List<Long> Ids);
    SeatInstanceResponse updateSeatInstanceStatus(Long id, SeatAvailabilityStatus status);
    Long countAvailableByFlightId(Long flightId);
    Double calculateSeatPrice(List<Long> seatInstanceId);
}
