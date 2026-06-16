package com.jenu.service.impl;

import com.jenu.exception.AirportException;
import com.jenu.mapper.FlightInstanceMapper;
import com.jenu.model.Flight;
import com.jenu.model.FlightInstance;
import com.jenu.payload.request.FlightInstanceRequest;
import com.jenu.payload.response.AircraftResponse;
import com.jenu.payload.response.AirlineResponse;
import com.jenu.payload.response.AirportResponse;
import com.jenu.payload.response.FlightInstanceResponse;
import com.jenu.repository.FlightInstanceRepository;
import com.jenu.repository.FlightRepository;
import com.jenu.service.FlightInstanceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightInstanceServiceImpl implements FlightInstanceService {

    private final FlightInstanceRepository flightInstanceRepository;
    private final FlightRepository flightRepository;

    @Override
    @Transactional
    public FlightInstanceResponse createFlightInstance(Long airlineId, FlightInstanceRequest request)  {
      //todo:watch airlineId
        Flight flight=flightRepository.findById(request.getFlightId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Flight not found")
                );
        //todo:service to service communication
        AircraftResponse aircraftResponse=AircraftResponse
                .builder()
                .id(1L)
                .totalSeats(90)
                .build();

        FlightInstance flightInstance= FlightInstanceMapper
                .convertToFlightInstanceEntity(request,flight);
        flightInstance.setTotalSeats(aircraftResponse.getTotalSeats());
        flightInstance.setAvailableSeats(aircraftResponse.getTotalSeats());

        FlightInstance savedFlightInstance=flightInstanceRepository.save(flightInstance);

        //todo : create seat instances

        return convertFlightInstanceResponse(savedFlightInstance);
    }

    @Override
    public List<FlightInstanceResponse> getFlightInstances() {
        return flightInstanceRepository.findAll().stream()
                .map(this::convertFlightInstanceResponse).toList();
    }

    @Override
    public FlightInstanceResponse getFlightInstanceById(Long id)  {
       FlightInstance flightInstance=flightInstanceRepository
               .findById(id)
               .orElseThrow(
                       ()->new EntityNotFoundException("Flight Instance not found with Id"+id)
               );

        return convertFlightInstanceResponse(flightInstance);
    }

    @Override
    public Page<FlightInstanceResponse> getByAirlineId(Long airlineId,
                                                       Long departureAirportId,
                                                       Long arrivalAirportId,
                                                       Long flightId,
                                                       LocalDate onDate,
                                                       Pageable pageable) {
        //todo: watch airlineId
        LocalDateTime start=onDate!= null ? onDate.atStartOfDay() : null;
        LocalDateTime end=onDate!=null ?onDate.plusDays(1).atStartOfDay(): null;


        return flightInstanceRepository.findByAirlineId(
                airlineId,departureAirportId,arrivalAirportId,flightId,start,end,pageable
        ).map(
                this::convertFlightInstanceResponse
        );
    }

    @Override
    public FlightInstanceResponse updateFlightInstance(Long id, FlightInstanceRequest flightInstanceRequest)  {
        FlightInstance existingFlightInstance=flightInstanceRepository
                .findById(id)
                .orElseThrow(
                        ()->new EntityNotFoundException("Flight instance is not found")
                );
        FlightInstanceMapper.updateEntity(flightInstanceRequest,existingFlightInstance);
        return convertFlightInstanceResponse(flightInstanceRepository.save(existingFlightInstance));
    }


    @Override
    public void deleteFlightInstance(Long id)  {
        FlightInstance existingFlightInstance=flightInstanceRepository
                .findById(id)
                .orElseThrow(
                        ()->new EntityNotFoundException("Flight instance is not found")
                );
        flightInstanceRepository.delete(existingFlightInstance);
    }

    @Override
    public Map<Long, FlightInstanceResponse> getFlightInstancesByIds(List<Long> ids) {
       /*This method implements once interservice connection could be completed*/
        return Map.of();
    }

    private FlightInstanceResponse convertFlightInstanceResponse
            (FlightInstance flightInstance) {
        //todo:service to service communication
        AirlineResponse airlineResponse=AirlineResponse
                .builder()
                .id(flightInstance.getAirlineId())
                .build();
        AirportResponse departureAirport=AirportResponse
                .builder()
                .id(flightInstance.getDepartureAirportId())
                .build();
        AirportResponse arrivalAirport=AirportResponse
                .builder()
                .id(flightInstance.getArrivalAirportId())
                .build();
        AircraftResponse aircraftResponse=AircraftResponse
                .builder()
                .id(flightInstance.getFlight().getId())
                .build();

        return FlightInstanceMapper
                .convertToFlightInstanceResponse(
                        flightInstance,
                        aircraftResponse,
                        airlineResponse,
                        departureAirport,
                        arrivalAirport);

    }
}
