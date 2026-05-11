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
    AirlineResponse getAirlineByOwner(Long ownerId) throws Exception;
    AirlineResponse updateAirline(AirlineRequest airlineRequest,Long ownerId) throws Exception;
    AirlineResponse getAirlineById(Long airlineId) throws Exception;
    Page<AirlineResponse> getAllAirlines(Pageable pageable);
    void deleteAirline(Long airlineId,Long ownerId) throws Exception;
    AirlineResponse changeStatusByAdmin(Long airlineId, AirlineStatus status) throws Exception;
    List<AirlineDropdownItem>getAirlineDropdown();
}
