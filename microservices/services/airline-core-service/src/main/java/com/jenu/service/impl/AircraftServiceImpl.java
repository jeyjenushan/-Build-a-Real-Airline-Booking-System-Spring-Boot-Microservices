package com.jenu.service.impl;

import com.jenu.exception.ResourceNotFoundException;
import com.jenu.mapper.AircraftMapper;
import com.jenu.model.Aircraft;
import com.jenu.model.Airline;
import com.jenu.payload.request.AircraftRequest;
import com.jenu.payload.response.AircraftResponse;
import com.jenu.repository.AircraftRepository;
import com.jenu.repository.AirlineRepository;
import com.jenu.service.AircraftService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AircraftServiceImpl implements AircraftService {

    private final AircraftRepository aircraftRepository;
    private final AirlineRepository airlineRepository;

    @Override
    public AircraftResponse createAircraft(AircraftRequest aircraftRequest, Long ownerId) {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(
                        ()-> new EntityNotFoundException("Airline not found for owner: " + ownerId));

        Aircraft aircraft = AircraftMapper.convertToAircraft(aircraftRequest,airline);

        if(aircraftRepository.existsByCode(aircraft.getCode())){
            throw new IllegalArgumentException("Aircraft with code " + aircraft.getCode() + " already exists");
        }
        validateAircraftData(aircraft);
        return AircraftMapper.convertToAircraftResponse(
                aircraftRepository.save(aircraft)
        );
    }

    @Override
    public AircraftResponse getAircraft(Long id) throws ResourceNotFoundException {
        return AircraftMapper.convertToAircraftResponse(
                aircraftRepository.findById(id)
                        .orElseThrow
                                (() -> new ResourceNotFoundException("aircraft not exist for this id"))
        );
    }

    @Override
    public List<AircraftResponse> listAllAircraftByOwnerId(Long ownerId)  {

        Airline airline=airlineRepository.findByOwnerId(ownerId)
                .orElseThrow
                        (() -> new EntityNotFoundException("This owner don't have airline"));
        return aircraftRepository.findByAirlineId(airline.getId())
                .stream()
                .map(AircraftMapper::convertToAircraftResponse).toList();


    }

    @Override
    public AircraftResponse updateAircraft(Long id,AircraftRequest aircraftRequest, Long ownerId) throws ResourceNotFoundException {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found for owner: " + ownerId));

        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + id));

        String oldCode = aircraft.getCode();
        AircraftMapper.updateEntity(aircraft, aircraftRequest, airline);

        if (!oldCode.equals(aircraftRequest.getCode()) && aircraftRepository.existsByCode(aircraftRequest.getCode())) {
            throw new IllegalArgumentException("Aircraft with code " + aircraftRequest.getCode() + " already exists");
        }

        validateAircraftData(aircraft);
        return AircraftMapper.convertToAircraftResponse(aircraftRepository.save(aircraft));
    }

    @Override
    public void deleteAircraft(Long id, Long ownerId) throws ResourceNotFoundException {
        Airline airline=airlineRepository.findByOwnerId(ownerId)
                .orElseThrow
                        (() -> new ResourceNotFoundException("This owner don't have airline"));
        Aircraft aircraft=aircraftRepository.findByIdAndAirlineId(id,airline.getId());
        if(aircraft==null){
            throw new ResourceNotFoundException("aircraft not exist for this id");
        }
        aircraftRepository.delete(aircraft);
    }


    private void validateAircraftData(Aircraft aircraft) {
        if (aircraft.getSeatingCapacity() != null && aircraft.getSeatingCapacity() <= 0) {
            throw new IllegalArgumentException("Seating capacity must be positive");
        }

        int totalSpecifiedSeats = (aircraft.getEconomySeats() != null ? aircraft.getEconomySeats() : 0) +
                (aircraft.getPremiumEconomySeats() != null ? aircraft.getPremiumEconomySeats() : 0) +
                (aircraft.getBusinessSeats() != null ? aircraft.getBusinessSeats() : 0) +
                (aircraft.getFirstClassSeats() != null ? aircraft.getFirstClassSeats() : 0);

        if (totalSpecifiedSeats > aircraft.getSeatingCapacity()) {
            throw new IllegalArgumentException("Total specified seats exceed aircraft seating capacity");
        }

        if (aircraft.getYearOfManufacture() != null &&
                (aircraft.getYearOfManufacture() < 1900
                        || aircraft.getYearOfManufacture() > LocalDate.now().getYear())) {
            throw new IllegalArgumentException("Invalid year of manufacture");
        }
    }
}
