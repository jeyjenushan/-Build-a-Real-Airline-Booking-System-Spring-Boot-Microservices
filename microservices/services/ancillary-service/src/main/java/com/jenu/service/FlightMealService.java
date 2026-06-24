package com.jenu.service;

import com.jenu.payload.request.FlightMealRequest;
import com.jenu.payload.response.FlightMealResponse;

import java.util.List;

public interface FlightMealService {

    FlightMealResponse create(FlightMealRequest request) throws Exception;

    List<FlightMealResponse> bulkCreate(List<FlightMealRequest> requests) throws Exception;

    FlightMealResponse getById(Long id) throws Exception;

    List<FlightMealResponse> getByFlightId(Long flightId);

    List<FlightMealResponse> getAllByIds(List<Long> Ids);

    FlightMealResponse update(Long id, FlightMealRequest request) throws Exception;

    void delete(Long id) throws Exception;

    FlightMealResponse updateAvailability(Long id, Boolean available) throws Exception;

    Double calculateMealPrice(List<Long> mealIds);
}
