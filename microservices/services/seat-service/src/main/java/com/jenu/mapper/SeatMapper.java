package com.jenu.mapper;


import com.jenu.model.CabinClass;
import com.jenu.model.Seat;
import com.jenu.model.SeatMap;
import com.jenu.payload.request.SeatRequest;
import com.jenu.payload.response.SeatResponse;

public class SeatMapper {


    public static void updateEntity(SeatRequest request, Seat seat, SeatMap seatMap, CabinClass cabinClass) {
        seat.setSeatNumber(request.getSeatNumber());
        seat.setSeatRow(request.getSeatRow());
        seat.setColumnLetter(request.getColumnLetter());
        seat.setSeatType(request.getSeatType());
        seat.setSeatMap(seatMap);
        seat.setCabinClass(cabinClass);
        if (request.getIsAvailable() != null) seat.setIsAvailable(request.getIsAvailable());
        if (request.getIsBlocked() != null) seat.setIsBlocked(request.getIsBlocked());
        if (request.getIsActive() != null) seat.setIsActive(request.getIsActive());
        seat.setBasePrice(request.getBasePrice());
        seat.setPremiumSupercharge(request.getPremiumSupercharge());
        if (request.getHasExtraLegroom() != null) seat.setHasExtraLegroom(request.getHasExtraLegroom());
        if (request.getHasPowerOutlet() != null) seat.setHasPowerOutlet(request.getHasPowerOutlet());
        if (request.getHasTvScreen() != null) seat.setHasTvScreen(request.getHasTvScreen());
        if (request.getHasExtraWidth() != null) seat.setHasExtraWidth(request.getHasExtraWidth());
        seat.setSeatPitch(request.getSeatPitch());
        seat.setSeatWidth(request.getSeatWidth());

    }

    public static SeatResponse toResponse(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .seatRow(seat.getSeatRow())
                .columnLetter(seat.getColumnLetter())
                .seatType(seat.getSeatType())
                .isAvailable(seat.getIsAvailable())
                .isBlocked(seat.getIsBlocked())

                .isActive(seat.getIsActive())
                .basePrice(seat.getBasePrice())
                .premiumSupercharge(seat.getPremiumSupercharge())
                .totalPrice(seat.getTotalPrice())
                .hasExtraLegroom(seat.getHasExtraLegroom())


                .hasPowerOutlet(seat.getHasPowerOutlet())
                .hasTvScreen(seat.getHasTvScreen())
                .hasExtraWidth(seat.getHasExtraWidth())
                .seatPitch(seat.getSeatPitch())
                .seatWidth(seat.getSeatWidth())

                .seatMapId(seat.getSeatMap() != null ? seat.getSeatMap().getId() : null)
                .seatMapName(seat.getSeatMap() != null ? seat.getSeatMap().getName() : null)
                .cabinClassId(seat.getCabinClass() != null ? seat.getCabinClass().getId() : null)
                .cabinClassName(seat.getCabinClass() != null ? seat.getCabinClass().getName().toString() : null)
                .createdAt(seat.getCreatedAt())
                .updatedAt(seat.getUpdatedAt())
                .createdBy(seat.getCreatedBy())
                .updatedBy(seat.getUpdatedBy())
                .isBookable(seat.isBookable())
                .fullPosition(seat.getFullPosition())
                .build();
    }
}

