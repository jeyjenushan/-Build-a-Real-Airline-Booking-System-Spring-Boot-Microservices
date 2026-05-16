package com.jenu.service.impl;

import com.jenu.enums.FlightStatus;
import com.jenu.mapper.FlightMapper;
import com.jenu.model.Flight;
import com.jenu.payload.request.FlightRequest;
import com.jenu.payload.response.AircraftResponse;
import com.jenu.payload.response.AirlineResponse;
import com.jenu.payload.response.AirportResponse;
import com.jenu.payload.response.FlightResponse;
import com.jenu.repository.FlightRepository;
import com.jenu.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;

    @Override
    public FlightResponse createFlight(Long airlineId, FlightRequest flightRequest) throws Exception {
       //todo:watch airlineId
        if(flightRepository.existsByFlightNumber(flightRequest.getFlightNumber())) {
            throw new Exception("Flight with number already exists");
        }
        Flight flight= FlightMapper.convertToFlightEntity(flightRequest);
        flight.setAirlineId(airlineId);
        Flight savedFlight = flightRepository.save(flight);
        return convertToFlightResponse(savedFlight);
    }

    @Override
    public Page<FlightResponse> getFlightsByAirline(Long airlineId, Long departureAirportId, Long arrivalAirportId, Pageable pageable) {
        //todo:watch airlineId
        return flightRepository.findByAirlineAndDepartureAirportAndArrivalAirportId(airlineId,departureAirportId,arrivalAirportId,pageable)
                .map(this::convertToFlightResponse);
    }

    @Override
    public FlightResponse getFlightById(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(
                        () -> new RuntimeException("Flight with id " + flightId + " not found")
                );
        return convertToFlightResponse(flight);
    }

    @Override
    public FlightResponse updateFlight(Long id, FlightRequest flightRequest) throws Exception {
        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Flight with id " + id + " not found")
                );
        if(flightRequest.getFlightNumber()!=null
        && flightRepository.existsByFlightNumberAndIdNot(flightRequest.getFlightNumber(),id)){
            throw new Exception("Flight with already exists");
        }
    FlightMapper.updateEntity(existingFlight,flightRequest);
        Flight updatedFlight = flightRepository.save(existingFlight);
        return convertToFlightResponse(updatedFlight);

    }

    @Override
    public void deleteFlight(Long airlineId,Long flightId) {
       //todo:watch airlineId
        Flight existingFlight = flightRepository.findByAirlineIdAndId(airlineId, flightId)
                .orElseThrow(
                        () -> new RuntimeException("Flight with id " + flightId + " not found")
                );
        flightRepository.delete(existingFlight);
    }


    @Override
    public FlightResponse changeStatus(Long flightId, FlightStatus flightStatus) {
        Flight existingFlight = flightRepository.findById(flightId)
                .orElseThrow(
                        () -> new RuntimeException("Flight with id " + flightId + " not found")
                );
        existingFlight.setStatus(flightStatus);
        Flight updatedFlight = flightRepository.save(existingFlight);
        return convertToFlightResponse(updatedFlight);
    }

    public FlightResponse convertToFlightResponse(Flight flight) {
        //todo: service to service communication
        AircraftResponse aircraftResponse=AircraftResponse
                .builder()
                .id(flight.getAircraftId())
                .build();
        AirlineResponse airlineResponse=AirlineResponse
                .builder()
                .id(flight.getAirlineId())
                .build();
        AirportResponse departureAirport=AirportResponse
                .builder()
                .id(flight.getDepartureAirportId())
                .build();
        AirportResponse arrivalAirport=AirportResponse
                .builder()
                .id(flight.getArrivalAirportId())
                .build();

        return FlightMapper
                .convertToFlightResponse(
                        flight,
                        aircraftResponse,
                        airlineResponse,
                        departureAirport,
                        arrivalAirport);
    }
}
