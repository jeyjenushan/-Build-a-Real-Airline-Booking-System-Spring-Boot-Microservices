package com.jenu.service;

import com.jenu.payload.request.SeatRequest;
import com.jenu.payload.response.SeatResponse;

import java.util.List;

public interface SeatService {


    void generateSeats(Long seatMapId) throws Exception;
    SeatResponse getSeatById(Long id);
    List<SeatResponse> getAll();
    SeatResponse updateSeat(Long id, SeatRequest request);

}
