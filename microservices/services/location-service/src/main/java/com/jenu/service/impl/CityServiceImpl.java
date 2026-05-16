package com.jenu.service.impl;

import com.jenu.mapper.CityMapper;
import com.jenu.model.City;
import com.jenu.payload.request.CityRequest;
import com.jenu.payload.response.CityResponse;
import com.jenu.repository.CityRepository;
import com.jenu.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private  final CityRepository cityRepository;

    @Override
    public CityResponse createCity(CityRequest cityRequest) throws Exception {

        if(cityRepository.existsByCityCode(cityRequest.getCityCode())){
            throw new Exception("City with given code already exist");
        }
        City city = CityMapper.ConvertToCity(cityRequest);
        City result = cityRepository.save(city);
        return CityMapper.ConvertToCityResponse(result);
    }

    @Override
    public CityResponse getCityById(Long id) throws Exception {
        City city = cityRepository.findById(id).orElseThrow(
                ()->new Exception("city not exist with given id")
        );
        return CityMapper.ConvertToCityResponse(city);

    }

    @Override
    public CityResponse updateCity(Long id, CityRequest cityRequest) throws Exception {
        City city = cityRepository.findById(id).orElseThrow(
                ()->new Exception("city not exist with given id")
        );
        if(cityRepository.existsByCityCodeAndIdNot(city.getCityCode(),id)){
            throw new Exception("City with given code already exist");
        }
        City updatedCity=cityRepository.save(CityMapper.updateCity(city,cityRequest));
        return CityMapper.ConvertToCityResponse(updatedCity);
    }

    @Override
    public void deleteCity(Long id) throws Exception {
        City city = cityRepository.findById(id).orElseThrow(
                ()->new Exception("city not exist with given id")
        );
        cityRepository.delete(city);
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

}
