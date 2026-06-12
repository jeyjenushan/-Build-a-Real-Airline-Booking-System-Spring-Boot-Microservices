package com.jenu.service;

import com.jenu.enums.AirlineStatus;
import com.jenu.payload.request.AirlineRequest;
import com.jenu.payload.response.AirlineDropdownItem;
import com.jenu.payload.response.AirlineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AirlineService {

    AirlineResponse createAirline(AirlineRequest airlineRequest,Long ownerId);
    AirlineResponse getAirlineByOwner(Long ownerId) ;
    AirlineResponse updateAirline(AirlineRequest airlineRequest,Long ownerId) ;
    AirlineResponse getAirlineById(Long airlineId) ;
    Page<AirlineResponse> getAllAirlines(Pageable pageable);
    void deleteAirline(Long airlineId,Long ownerId) ;
    AirlineResponse changeStatusByAdmin(Long airlineId, AirlineStatus status) ;
    List<AirlineDropdownItem>getAirlineDropdown();
}
