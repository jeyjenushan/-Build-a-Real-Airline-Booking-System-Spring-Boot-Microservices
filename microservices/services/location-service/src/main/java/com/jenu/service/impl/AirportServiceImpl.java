package com.jenu.service.impl;

import com.jenu.mapper.AirportMapper;
import com.jenu.model.Airport;
import com.jenu.model.City;
import com.jenu.payload.request.AirportRequest;
import com.jenu.payload.response.AirportResponse;
import com.jenu.repository.AirportRepository;
import com.jenu.repository.CityRepository;
import com.jenu.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;

    @Override
    public AirportResponse createAirport(AirportRequest airportRequest) throws Exception {
        if (airportRepository.findByIataCode(airportRequest.getIataCode()).isPresent()) {
            throw new Exception("Airport with Iata Code Already Exist");
        }
        City city=cityRepository.findById(airportRequest.getCityId()).orElseThrow(
                () -> new Exception("City not found")
        );
        Airport airport= AirportMapper.ConvertToAirport(airportRequest);
        airport.setCity(city);
        return AirportMapper.ConvertToAirportResponse(airportRepository.save(airport));

    }

    @Override
    public AirportResponse getAirportById(Long id) throws Exception {
        return AirportMapper.ConvertToAirportResponse(airportRepository.findById(id).orElseThrow(
                ()->new Exception("Airport not exists with provided id")
                ));
    }

    @Override
    public List<AirportResponse> getAllAirports() {
        return airportRepository.findAll().stream().map(AirportMapper::ConvertToAirportResponse).collect(Collectors.toList());
    }

    @Override
    public AirportResponse updateAirport(Long id, AirportRequest airportRequest) throws Exception {
        Airport existingAirport = airportRepository.findById(id).orElseThrow(
                ()->new Exception("airport not exists with id"+id)
        );
        if(airportRequest.getIataCode()!=null && !airportRequest.getIataCode().equals(existingAirport.getIataCode())
        && airportRepository.findByIataCode(airportRequest.getIataCode()).isPresent()
        ){
            throw new Exception("Airport with Iata Code Already Exist");
        }
        return AirportMapper.ConvertToAirportResponse(AirportMapper.updateEntity(airportRequest, existingAirport));

    }

    @Override
    public void deleteAirport(Long id) throws Exception {
        Airport airport=airportRepository.findById(id).orElseThrow(
                ()->new Exception("Airport not exists with provided id")
        );
        airportRepository.delete(airport);

    }

    @Override
    public List<AirportResponse> getAirportsByCityId(Long cityId) {
        return airportRepository.findByCityId(cityId).stream().map(AirportMapper::ConvertToAirportResponse).collect(Collectors.toList());
    }
}
