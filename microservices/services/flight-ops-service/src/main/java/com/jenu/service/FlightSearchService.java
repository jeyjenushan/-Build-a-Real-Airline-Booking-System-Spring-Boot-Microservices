package com.jenu.service;

import com.jenu.payload.request.FlightSearchRequest;
import com.jenu.payload.response.FlightInstanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlightSearchService {

Page<FlightInstanceResponse> searchFlights(FlightSearchRequest request, Pageable pageable);

}
