package com.jenu.mapper;


import com.jenu.enums.SeatAvailabilityStatus;
import com.jenu.model.FlightInstanceCabin;
import com.jenu.model.Seat;
import com.jenu.model.SeatInstance;
import com.jenu.payload.request.SeatInstanceRequest;
import com.jenu.payload.response.SeatInstanceResponse;

public class SeatInstanceMapper {

    public static SeatInstance toEntity(SeatInstanceRequest request, Seat seat,
                                        FlightInstanceCabin flightInstanceCabin) {
        return SeatInstance.builder()
                .flightId(request.getFlightId())
                .seat(seat)
                .flightInstanceCabin(flightInstanceCabin)
                .flightInstanceId(request.getFlightInstanceId())
                .status(request.getStatus() != null ?
                        SeatAvailabilityStatus.valueOf(request.getStatus().toUpperCase()) :
                        SeatAvailabilityStatus.AVAILABLE)
                .mealPreference(request.getMealPreference())
                .fare(request.getFare())
                .flightScheduleId(request.getFlightScheduleId())
                .build();
    }

    public static SeatInstanceResponse toResponse(SeatInstance si) {
        return SeatInstanceResponse.builder()
                .id(si.getId())
                .flightId(si.getFlightId())
                .seatId(si.getSeat() != null ? si.getSeat().getId() : null)
                .seatNumber(si.getSeat() != null ? si.getSeat().getSeatNumber() : null)
                .seatType(si.getSeat() != null ? si.getSeat().getSeatType().name() : null)
                .seatPosition(si.getSeat() != null ? si.getSeat().getFullPosition() : null)
                .seat(SeatMapper.toResponse(si.getSeat()))
                .status(si.getStatus())
                .flightInstanceId(si.getFlightInstanceId())
                .flightCabinId(si.getFlightInstanceCabin() != null ? si.getFlightInstanceCabin().getId() : null)

                .fare(si.getFare())
                .price(si.getPremiumSurcharge())
                .version(si.getVersion())
                .createdAt(si.getCreatedAt())
                .updatedAt(si.getUpdatedAt())
                .isAvailable(si.isAvailable())
                .isBooked(si.isBooked())
                .isOccupied(si.getStatus()== SeatAvailabilityStatus.OCCUPIED)
                .build();
    }
}

