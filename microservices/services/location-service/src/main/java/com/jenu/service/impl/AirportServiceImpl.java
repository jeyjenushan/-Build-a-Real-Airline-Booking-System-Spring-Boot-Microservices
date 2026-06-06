package com.jenu.service.impl;

import com.jenu.exception.AirportException;
import com.jenu.exception.CityException;
import com.jenu.mapper.AirportMapper;
import com.jenu.model.Airport;
import com.jenu.model.City;
import com.jenu.payload.request.AirportRequest;
import com.jenu.payload.response.AirportResponse;
import com.jenu.repository.AirportRepository;
import com.jenu.repository.CityRepository;
import com.jenu.service.AirportService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;

    @Override
    public AirportResponse createAirport(AirportRequest airportRequest) throws AirportException, CityException {
        if (airportRepository.findByIataCode(airportRequest.getIataCode()).isPresent()) {
            throw new AirportException("Airport with Iata Code Already Exist");
        }
        City city=cityRepository.findById(airportRequest.getCityId()).orElseThrow(
                () -> new CityException("City not found")
        );
        Airport airport= AirportMapper.ConvertToAirport(airportRequest);
        airport.setCity(city);
        return AirportMapper.ConvertToAirportResponse(airportRepository.save(airport));

    }

    @Override
    @Transactional
    public List<AirportResponse> createBulkAirports(List<AirportRequest> requests)
            throws AirportException, CityException {
        List<AirportResponse> createdAirports = new ArrayList<>();
        List<String> skippedCodes = new ArrayList<>();

        for (AirportRequest request : requests) {
            if (airportRepository.findByIataCode(request.getIataCode()).isPresent()) {
                skippedCodes.add(request.getIataCode() + " (already exists)");
                continue;
            }

            Optional<City> cityOpt = cityRepository.findById(request.getCityId());
            if (cityOpt.isEmpty()) {
                skippedCodes.add(request.getIataCode() + " (city not found with id: " + request.getCityId() + ")");
                continue;
            }

            Airport airport = AirportMapper.ConvertToAirport(request);
            airport.setCity(cityOpt.get());

            Airport savedAirport = airportRepository.save(airport);
            createdAirports.add(AirportMapper.ConvertToAirportResponse(savedAirport));
        }

        if (!skippedCodes.isEmpty()) {
            log.info("Bulk airport creation - skipped: {}", skippedCodes);
        }
        log.info("Bulk airport creation - created {} out of {} airports", createdAirports.size(), requests.size());

        return createdAirports;
    }

    @Override
    public AirportResponse getAirportById(Long id) throws Exception {
        return AirportMapper.ConvertToAirportResponse(airportRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Airport not exists with provided id")
                ));
    }

    @Override
    public List<AirportResponse> getAllAirports() {
        return airportRepository.findAll().stream().map(AirportMapper::ConvertToAirportResponse).collect(Collectors.toList());
    }

    @Override
    public AirportResponse updateAirport(Long id, AirportRequest airportRequest) throws AirportException,CityException {
        Airport existingAirport = airportRepository.findById(id).orElseThrow(
                ()->new AirportException("airport not exists with id"+id)
        );
        if (airportRequest.getIataCode() != null
                && !existingAirport.getIataCode().equals(airportRequest.getIataCode())
                && airportRepository.findByIataCode(airportRequest.getIataCode()).isPresent()) {
            throw new AirportException("IATA code " + airportRequest.getIataCode() + " is already taken.");
        }
        if (airportRequest.getCityId() != null) {
            City newCity = cityRepository.findById(airportRequest.getCityId())
                    .orElseThrow(() -> new CityException("City not found with id: " + airportRequest.getCityId()));
            existingAirport.setCity(newCity);
        }

        return AirportMapper.ConvertToAirportResponse(AirportMapper.updateEntity(airportRequest, existingAirport));

    }

    @Override
    public void deleteAirport(Long id) throws AirportException {
        Airport airport=airportRepository.findById(id).orElseThrow(
                ()->new AirportException("Airport not exists with provided id")
        );
        airportRepository.delete(airport);

    }

    @Override
    public List<AirportResponse> getAirportsByCityId(Long cityId) {
        return airportRepository.findByCityId(cityId).stream().map(AirportMapper::ConvertToAirportResponse).collect(Collectors.toList());
    }
}
