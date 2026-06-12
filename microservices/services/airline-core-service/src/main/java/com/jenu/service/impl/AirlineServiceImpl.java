package com.jenu.service.impl;

import com.jenu.enums.AirlineStatus;
import com.jenu.mapper.AirlineMapper;
import com.jenu.model.Airline;
import com.jenu.payload.request.AirlineRequest;
import com.jenu.payload.response.AirlineDropdownItem;
import com.jenu.payload.response.AirlineResponse;
import com.jenu.repository.AirlineRepository;
import com.jenu.service.AirlineService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;

    @Override
    public AirlineResponse createAirline(AirlineRequest airlineRequest, Long ownerId) {
        Airline airline = AirlineMapper.convertAirline(airlineRequest, ownerId);
        Airline saved=airlineRepository.save(airline);
        return AirlineMapper.convertAirlineResponse(airline);
    }

    @Override
    public AirlineResponse getAirlineByOwner(Long ownerId)  {
        Airline airline=airlineRepository.findByOwnerId(ownerId).orElseThrow(
                ()-> new EntityNotFoundException("Airline not found with ownerid "+ownerId)
        );
                return AirlineMapper.convertAirlineResponse(airline);
    }

    @Override
    public AirlineResponse updateAirline(AirlineRequest airlineRequest, Long ownerId)  {
        Airline airline=airlineRepository.findByOwnerId(ownerId).orElseThrow(
                ()-> new EntityNotFoundException("Airline not found with ownerid "+ownerId)
        );
        AirlineMapper.updateEntity(airline,airlineRequest);
        Airline savedAirline=airlineRepository.save(airline);
        return AirlineMapper.convertAirlineResponse(savedAirline);
    }

    @Override
    public AirlineResponse getAirlineById(Long airlineId)  {
        Airline airline=airlineRepository.findById(airlineId).orElseThrow(
                ()-> new EntityNotFoundException("Airline not found with id "+airlineId)
        );
        return AirlineMapper.convertAirlineResponse(airline);
    }

    @Override
    public Page<AirlineResponse> getAllAirlines(Pageable pageable) {
      return airlineRepository.findAll(pageable).map(AirlineMapper::convertAirlineResponse);
    }

    @Override
    public void deleteAirline(Long airlineId, Long ownerId)  {
        Airline airline=airlineRepository.findByOwnerId(ownerId).orElseThrow(
                ()-> new EntityNotFoundException("Airline not found with ownerid "+ownerId)
        );
        airlineRepository.delete(airline);

    }

    @Override
    public AirlineResponse changeStatusByAdmin(Long airlineId, AirlineStatus status)  {
        Airline airline=airlineRepository.findById(airlineId).orElseThrow(
                ()-> new EntityNotFoundException("Airline not found with id "+airlineId)
        );
      airline.setStatus(status);
      Airline savedAirline=airlineRepository.save(airline);
      return AirlineMapper.convertAirlineResponse(savedAirline);
    }

    @Override
    public List<AirlineDropdownItem> getAirlineDropdown() {
       return airlineRepository.findByStatus(AirlineStatus.ACTIVE)
               .stream()
               .map(Airline->AirlineDropdownItem.builder()
                       .id(Airline.getId())
                       .name(Airline.getName())
                       .iataCode(Airline.getIataCode())
                       .icaoCode(Airline.getIcaoCode())
                       .logoUrl(Airline.getLogoUrl())
                       .build()).toList();
    }
}