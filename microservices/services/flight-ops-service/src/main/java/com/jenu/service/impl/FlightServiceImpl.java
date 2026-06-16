package com.jenu.service.impl;

import com.jenu.enums.FlightStatus;
import com.jenu.exception.AirportException;
import com.jenu.mapper.FlightMapper;
import com.jenu.model.Flight;
import com.jenu.payload.request.FlightRequest;
import com.jenu.payload.response.AircraftResponse;
import com.jenu.payload.response.AirlineResponse;
import com.jenu.payload.response.AirportResponse;
import com.jenu.payload.response.FlightResponse;
import com.jenu.repository.FlightRepository;
import com.jenu.service.FlightService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;

    @Override
    public FlightResponse createFlight(Long userId, FlightRequest flightRequest)  {
       //todo:watch airlineId
        if (flightRepository.existsByFlightNumber(flightRequest.getFlightNumber())) {
            throw new IllegalArgumentException(
                    "Flight with number '" + flightRequest.getFlightNumber() + "' already exists");
        }
        Flight flight= FlightMapper.convertToFlightEntity(flightRequest);
        flight.setAirlineId(userId);
        Flight savedFlight = flightRepository.save(flight);
        return convertToFlightResponse(savedFlight);
    }

    @SneakyThrows
    @Override
    public List<FlightResponse> createFlights(Long userId, List<FlightRequest> requests)  {
        // Single DB call to find all already-existing flight numbers
        Set<String> existingNumbers = flightRepository.findExistingFlightNumbers(
                requests.stream().map(FlightRequest::getFlightNumber).collect(Collectors.toList()));

        // Validate each unique aircraftId once via Feign
        Set<Long> validatedAircraftIds = new HashSet<>();

        List<Flight> toSave = requests.stream()
                .filter(req -> !existingNumbers.contains(req.getFlightNumber()))
                .map(req -> {
                    if (validatedAircraftIds.add(req.getAircraftId())) {
                      //  validateAircraftExists(req.getAircraftId());
                    }
                    Flight flight = FlightMapper.convertToFlightEntity(req);
                    flight.setAirlineId(userId);
                    return flight;
                })
                .collect(Collectors.toList());

        List<Flight> saved = flightRepository.saveAll(toSave);

        /*
        * Once interconnected work on this api
        *
        * */



        return List.of();
    }

    /*
    This Function for getAirport with location once inter service connection started work on this
    *     private AirportResponse getAirport(Long id) {
            return locationClient.getAirportById(id);
    }
    *
    * */


    @Override
    public FlightResponse getFlightById(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Flight with id " + flightId + " not found")
                );
        return convertToFlightResponse(flight);
    }

    @Override
    @Transactional(readOnly = true)
    public FlightResponse getFlightByNumber(String flightNumber) {
        Flight flight = flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Flight not found with number: " + flightNumber));
        return convertToFlightResponse(flight);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlightResponse> getFlightsByAirline(Long userId, Long departureAirportId, Long arrivalAirportId, Pageable pageable) {
        //todo:watch airlineId
        return flightRepository.findByAirlineAndDepartureAirportAndArrivalAirportId(userId,departureAirportId,arrivalAirportId,pageable)
                .map(this::convertToFlightResponse);
    }




    @Override
    public FlightResponse updateFlight(Long id, FlightRequest flightRequest)  {
        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Flight with id " + id + " not found")
                );
        if (flightRequest.getFlightNumber() != null &&
                flightRepository.existsByFlightNumberAndIdNot(flightRequest.getFlightNumber(), id)) {
            throw new IllegalArgumentException(
                    "Flight with number '" + flightRequest.getFlightNumber() + "' already exists");
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
                        () -> new EntityNotFoundException("Flight with id " + flightId + " not found")
                );
        flightRepository.delete(existingFlight);
    }


    @Override
    public FlightResponse changeStatus(Long flightId, FlightStatus flightStatus) {
        Flight existingFlight = flightRepository.findById(flightId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Flight with id " + flightId + " not found")
                );
        existingFlight.setStatus(flightStatus);
        Flight updatedFlight = flightRepository.save(existingFlight);
        return convertToFlightResponse(updatedFlight);
    }

    @Override
    public Map<Long, FlightResponse> getFlightsByIds(List<Long> ids) {
        //todo:Once interservice connection implemented I work on this
        return Map.of();
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
