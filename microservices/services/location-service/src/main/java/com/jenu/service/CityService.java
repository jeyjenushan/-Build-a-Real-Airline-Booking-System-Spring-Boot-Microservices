package com.jenu.service;

import com.jenu.exception.OperationNotPermittedException;
import com.jenu.exception.ResourceNotFoundException;
import com.jenu.payload.request.CityRequest;
import com.jenu.payload.response.CityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CityService {

    CityResponse createCity(CityRequest cityRequest) throws OperationNotPermittedException;
    List<CityResponse> createBulkCities(List<CityRequest> cityRequests) throws OperationNotPermittedException;
    CityResponse getCityById(Long id) throws ResourceNotFoundException;
    CityResponse updateCity(Long id, CityRequest cityRequest) throws ResourceNotFoundException, OperationNotPermittedException;
    void deleteCity(Long id) throws ResourceNotFoundException;
    Page<CityResponse> getAllCities(Pageable pageable);
    Page<CityResponse> searchCities(String keyword, Pageable pageable);
    Page<CityResponse> getCitiesByCountryCode(String countryCode, Pageable pageable);
    boolean cityExists(String cityCode);
    boolean validateCityCode(String cityCode);




}
