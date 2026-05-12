package com.jenu.service.impl;

import com.jenu.mapper.AircraftMapper;
import com.jenu.model.Aircraft;
import com.jenu.model.Airline;
import com.jenu.payload.request.AircraftRequest;
import com.jenu.payload.response.AircraftResponse;
import com.jenu.repository.AircraftRepository;
import com.jenu.repository.AirlineRepository;
import com.jenu.service.AircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AircraftServiceImpl implements AircraftService {

    private final AircraftRepository aircraftRepository;
    private final AirlineRepository airlineRepository;

    @Override
    public AircraftResponse createAircraft(AircraftRequest aircraftRequest, Long ownerId) throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(
                        ()->new Exception("airline not exist for this ownerId")
                );
        Aircraft aircraft = AircraftMapper.convertToAircraft(aircraftRequest,airline);

        if(aircraftRepository.existsByCode(aircraft.getCode())){
            throw new Exception("code already exists with another aircraft");
        }
        if(aircraft.getSeatingCapacity()<aircraft.getTotalSeats()){
            throw new Exception("seating capacity can't exceed to total seat");
        }
        return AircraftMapper.convertToAircraftResponse(
                aircraftRepository.save(aircraft)
        );
    }

    @Override
    public AircraftResponse getAircraft(Long id) throws Exception {
        return AircraftMapper.convertToAircraftResponse(
                aircraftRepository.findById(id)
                        .orElseThrow
                                (() -> new Exception("aircraft not exist for this id"))
        );
    }

    @Override
    public List<AircraftResponse> listAllAircraftByOwnerId(Long ownerId) throws Exception {

        Airline airline=airlineRepository.findByOwnerId(ownerId)
                .orElseThrow
                        (() -> new Exception("This owner don't have airline"));
        return aircraftRepository.findByAirlineId(airline.getId())
                .stream()
                .map(AircraftMapper::convertToAircraftResponse).toList();


    }

    @Override
    public AircraftResponse updateAircraft(Long id,AircraftRequest aircraftRequest, Long ownerId) throws Exception {
        Airline airline=airlineRepository.findByOwnerId(ownerId)
                .orElseThrow
                        (() -> new Exception("This owner don't have airline"));
        Aircraft aircraft=aircraftRepository.findByIdAndAirlineId(id,airline.getId());
        if(aircraft==null){
            throw new Exception("aircraft not exist for this id");
        }
        if(aircraftRequest.getCode()!=null && !aircraft.getCode().equals(aircraftRequest.getCode())
                && aircraftRepository.existsByCode(aircraft.getCode())){
            throw new Exception("code already exists with another aircraft");
        }
        AircraftMapper.updateEntity(aircraft,aircraftRequest);

        return AircraftMapper.convertToAircraftResponse(
                aircraftRepository.save(aircraft)
        );
    }

    @Override
    public void deleteAircraft(Long id, Long ownerId) throws Exception {
        Airline airline=airlineRepository.findByOwnerId(ownerId)
                .orElseThrow
                        (() -> new Exception("This owner don't have airline"));
        Aircraft aircraft=aircraftRepository.findByIdAndAirlineId(id,airline.getId());
        if(aircraft==null){
            throw new Exception("aircraft not exist for this id");
        }
        aircraftRepository.delete(aircraft);
    }
}
