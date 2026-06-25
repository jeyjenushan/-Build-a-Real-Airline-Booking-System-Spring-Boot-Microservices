package com.jenu.service.impl;

import com.jenu.client.AirlineClient;
import com.jenu.client.LocationClient;
import com.jenu.event.FlightInstanceCreatedEvent;
import com.jenu.event.FlightInstanceEventProducer;
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
    private final AirlineClient airlineClient;
    private final LocationClient locationClient;
    private final FlightInstanceEventProducer flightInstanceEventProducer;

    @Override
    @Transactional
    public FlightInstanceResponse createFlightInstance(Long userId, FlightInstanceRequest request)  {
        AirlineResponse airlineResponse=airlineClient.getAirlineByOwner(userId);
        Flight flight=flightRepository.findById(request.getFlightId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Flight not found")
                );

        AircraftResponse aircraftResponse= airlineClient.getAircraft(flight.getAircraftId());

        FlightInstance flightInstance= FlightInstanceMapper
                .convertToFlightInstanceEntity(request,flight);
        flightInstance.setTotalSeats(aircraftResponse.getTotalSeats());
        flightInstance.setAvailableSeats(aircraftResponse.getTotalSeats());

        FlightInstance savedFlightInstance=flightInstanceRepository.save(flightInstance);


        //Publish kafka event, seat service consume that and creates seat instances
        FlightInstanceCreatedEvent flightInstanceCreatedEvent=FlightInstanceCreatedEvent
                .builder()
                .flightInstanceId(savedFlightInstance.getId())
                .aircraftId(flightInstance.getFlight().getAircraftId())
                .flightId(flight.getId())
                .build();

        flightInstanceEventProducer.sendFlightInstanceCreatedEvent(flightInstanceCreatedEvent);


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
    public Page<FlightInstanceResponse> getByAirlineId(Long userId,
                                                       Long departureAirportId,
                                                       Long arrivalAirportId,
                                                       Long flightId,
                                                       LocalDate onDate,
                                                       Pageable pageable) {
        AirlineResponse airlineResponse=airlineClient.getAirlineByOwner(userId);
        LocalDateTime start=onDate!= null ? onDate.atStartOfDay() : null;
        LocalDateTime end=onDate!=null ?onDate.plusDays(1).atStartOfDay(): null;


        return flightInstanceRepository.findByAirlineId(
                airlineResponse.getId(),departureAirportId,arrivalAirportId,flightId,start,end,pageable
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
        AirlineResponse airlineResponse=airlineClient.getAirlineById(flightInstance.getFlight().getAirlineId());
        AirportResponse departureAirport=locationClient.getAirportById(flightInstance.getDepartureAirportId());
        AirportResponse arrivalAirport=locationClient.getAirportById(flightInstance.getArrivalAirportId());
        AircraftResponse aircraftResponse=airlineClient.getAircraft(flightInstance.getFlight().getAircraftId());

        return FlightInstanceMapper
                .convertToFlightInstanceResponse(
                        flightInstance,
                        aircraftResponse,
                        airlineResponse,
                        departureAirport,
                        arrivalAirport);

    }
}
