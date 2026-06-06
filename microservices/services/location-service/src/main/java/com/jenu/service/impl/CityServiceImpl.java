package com.jenu.service.impl;

import com.jenu.exception.OperationNotPermittedException;
import com.jenu.exception.ResourceNotFoundException;
import com.jenu.mapper.CityMapper;
import com.jenu.model.City;
import com.jenu.payload.request.CityRequest;
import com.jenu.payload.response.CityResponse;
import com.jenu.repository.CityRepository;
import com.jenu.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CityServiceImpl implements CityService {

    private  final CityRepository cityRepository;



    @Override
    public CityResponse createCity(CityRequest cityRequest) throws OperationNotPermittedException {

        validateCityRequest(cityRequest);

        if(cityRepository.existsByCityCode(cityRequest.getCityCode())){
            throw new OperationNotPermittedException("City with given code already exist");
        }
        City city = CityMapper.ConvertToCity(cityRequest);
        City savedCity = cityRepository.save(city);
        log.info("City created: {} ({})", savedCity.getName(), savedCity.getCityCode());
        return CityMapper.ConvertToCityResponse(savedCity);
    }

    @Override
    public List<CityResponse> createBulkCities(List<CityRequest> cityRequests) throws OperationNotPermittedException {
       List<CityResponse> createdCities = new ArrayList<>();
       List<String> skippedCityCodes = new ArrayList<String>();

       for (CityRequest cityRequest : cityRequests) {
           try {
               validateCityRequest(cityRequest);
           } catch (IllegalArgumentException e) {
               skippedCityCodes.add(cityRequest.getCityCode() + " (invalid: " + e.getMessage() + ")");
               continue;
           }

           if (cityRepository.existsByCityCode(cityRequest.getCityCode())) {
               skippedCityCodes.add(cityRequest.getCityCode() + " (already exists)");
               continue;
           }
           City city = CityMapper.ConvertToCity(cityRequest);
           City savedCity = cityRepository.save(city);
           createdCities.add(CityMapper.ConvertToCityResponse(savedCity));

       }
        if (!skippedCityCodes.isEmpty()) {
            log.info("Bulk city creation - skipped: {}", skippedCityCodes);
        }
        log.info("Bulk city creation - created {} out of {} cities", createdCities.size(), cityRequests.size());

        return createdCities;


    }

    @Override
    public CityResponse getCityById(Long id) throws ResourceNotFoundException {
        City city = cityRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("city not exist with given id")
        );
        return CityMapper.ConvertToCityResponse(city);

    }

    @Override
    public CityResponse updateCity(Long id, CityRequest cityRequest) throws ResourceNotFoundException, OperationNotPermittedException {
        City city = cityRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("city not exist with given id")
        );
        validateCityRequest(cityRequest,id);
        if(cityRepository.existsByCityCodeAndIdNot(city.getCityCode(),id)){
            throw new ResourceNotFoundException("City with given code already exist");
        }
        City updatedCity=cityRepository.save(CityMapper.updateCity(city,cityRequest));
        log.info("City updated: {} ({})", updatedCity.getName(), updatedCity.getCityCode());
        return CityMapper.ConvertToCityResponse(updatedCity);
    }

    @Override
    public void deleteCity(Long id) throws ResourceNotFoundException {
        City city = cityRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("city not exist with given id")
        );
        cityRepository.delete(city);
        log.info("City deleted: {} ({})", city.getName(), city.getCityCode());
    }

    @Override
    public Page<CityResponse> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable).map(CityMapper::ConvertToCityResponse);
    }

    @Override
    public Page<CityResponse> searchCities(String keyword, Pageable pageable) {
        return cityRepository.searchByKeyword(keyword, pageable).map(CityMapper::ConvertToCityResponse);
    }

    @Override
    public Page<CityResponse> getCitiesByCountryCode(String countryCode, Pageable pageable) {
        return cityRepository.findByCountryCodeIgnoreCase(countryCode, pageable).map(CityMapper::ConvertToCityResponse);
    }

    @Override
    public boolean cityExists(String cityCode) {
        return cityRepository.existsByCityCode(cityCode);
    }

    @Override
    public boolean validateCityCode(String cityCode) {
        return cityCode != null && cityCode.length() <= 10 && cityCode.matches("[A-Z0-9]{2,10}");
    }


    private void validateCityRequest(CityRequest request) {
        validateCityRequest(request, null);
    }

    private void validateCityRequest(CityRequest request, Long excludeId) {
        if (!validateCityCode(request.getCityCode())) {
            throw new IllegalArgumentException("Invalid city code format. Must be 2-10 alphanumeric characters.");
        }

        if (request.getCountryCode() == null || !request.getCountryCode().matches("[A-Z]{2,5}")) {
            throw new IllegalArgumentException("Country code must be 2-5 uppercase letters");
        }

        if (request.getTimeZoneOffset() != null && !request.getTimeZoneOffset().matches("[+-]\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Time zone offset must be in format ±HH:MM");
        }
    }

}
