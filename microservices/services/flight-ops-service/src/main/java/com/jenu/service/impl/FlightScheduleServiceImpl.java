package com.jenu.service.impl;

import com.jenu.enums.FlightStatus;
import com.jenu.mapper.FlightInstanceMapper;
import com.jenu.mapper.FlightScheduleMapper;
import com.jenu.model.Flight;
import com.jenu.model.FlightSchedule;
import com.jenu.payload.request.FlightInstanceRequest;
import com.jenu.payload.request.FlightScheduleRequest;
import com.jenu.payload.response.AirportResponse;
import com.jenu.payload.response.FlightScheduleResponse;
import com.jenu.repository.FlightInstanceRepository;
import com.jenu.repository.FlightRepository;
import com.jenu.repository.FlightScheduleRepository;
import com.jenu.service.FlightInstanceService;
import com.jenu.service.FlightScheduleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightScheduleServiceImpl implements FlightScheduleService {

    private final FlightScheduleRepository flightScheduleRepository;
    private final FlightRepository flightRepository;
    private final FlightInstanceService flightInstanceService;


    @Override
    public FlightScheduleResponse createFlightSchedule(Long airlineId,
                                                       FlightScheduleRequest flightScheduleRequest) throws Exception {
        //todo watch for airlineId

        Flight flight=flightRepository.findById(flightScheduleRequest.getFlightId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Flight not found with given id")
                );
        if(flightScheduleRequest.getEndDate().isBefore(flightScheduleRequest.getStartDate())){
            throw new IllegalArgumentException("End date is before start date");
        }
        FlightSchedule flightSchedule= FlightScheduleMapper.convertToFlightSchedule(flightScheduleRequest,flight);
        FlightSchedule savedFlightSchedule=flightScheduleRepository.save(flightSchedule);

        //Create Flight instance saved schedule
        //11/03/2026 to 10/04/2026
        //mon,tue,wed,thu
        List<DayOfWeek> operatingDays=savedFlightSchedule.getOperatingDays();
        LocalDate startDate=savedFlightSchedule.getStartDate();
        LocalDate endDate=savedFlightSchedule.getEndDate();

        FlightInstanceRequest flightInstanceRequest=FlightInstanceRequest
                .builder()
                .scheduleId(savedFlightSchedule.getId())
                .flightId(flight.getId())
                .arrivalAirportId(flight.getArrivalAirportId())
                .departureAirportId(flight.getDepartureAirportId())
                .flightStatus(FlightStatus.SCHEDULED)
                .build();

        for(LocalDate date=startDate;!date.isAfter(endDate);date=date.plusDays(1)){
            if(operatingDays.contains(date.getDayOfWeek())){
                flightInstanceRequest.setDepartureDateTime(
                        LocalDateTime.of(date,savedFlightSchedule.getDepartureTime())
                );
                flightInstanceRequest.setArrivalDateTime(
                        LocalDateTime.of(date,savedFlightSchedule.getArrivalTime())
                );
                flightInstanceService.createFlightInstance(
                        airlineId,flightInstanceRequest
                );

            }
        }




        return convertFlightScheduleToFlightScheduleResponse(savedFlightSchedule);
    }

    @Override
    public FlightScheduleResponse getFlightSchedule(Long flightScheduleId)  {
        FlightSchedule flightSchedule=flightScheduleRepository
                .findById(flightScheduleId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Flight schedule not found with id: " + flightScheduleId));;
        return convertFlightScheduleToFlightScheduleResponse(flightSchedule);
    }

    @Override
    public List<FlightScheduleResponse> getAllFlightSchedulesByAirline(Long userId) {
        List<FlightSchedule> schedules=flightScheduleRepository
                .findByFlightAirlineId(userId);
        return schedules.stream()
                .map(this::convertFlightScheduleToFlightScheduleResponse).toList();
    }

    @Override
    public void deleteFlightSchedule(Long flightScheduleId)  {
        FlightSchedule flightSchedule=flightScheduleRepository
                .findById(flightScheduleId)
                .orElseThrow(
                        ()->new EntityNotFoundException("Flight schedule not found with id")
                );
        flightScheduleRepository.delete(flightSchedule);

    }

    @Override
    public FlightScheduleResponse updateFlightSchedule(Long id, FlightScheduleRequest flightScheduleRequest)  {
        FlightSchedule flightSchedule=flightScheduleRepository
                .findById(id)
                .orElseThrow(
                        ()->new EntityNotFoundException("Flight schedule not found with id")
                );
        FlightScheduleMapper.updateEntity(flightScheduleRequest,flightSchedule);
        FlightSchedule savedFlightSchedule=flightScheduleRepository.save(flightSchedule);
        return convertFlightScheduleToFlightScheduleResponse(savedFlightSchedule);
    }

    private FlightScheduleResponse convertFlightScheduleToFlightScheduleResponse(FlightSchedule flightSchedule) {
        // todo :Service to service communication
        AirportResponse departureAirport=AirportResponse.builder()
                .id(flightSchedule.getDepartureAirportId())
                .build();
        AirportResponse arrivalAirport=AirportResponse.builder()
                .id(flightSchedule.getArrivalAirportId())
                .build();
        return FlightScheduleMapper
                .convertToFlightScheduleResponse(
                        flightSchedule,arrivalAirport,departureAirport
                );
    }
}
