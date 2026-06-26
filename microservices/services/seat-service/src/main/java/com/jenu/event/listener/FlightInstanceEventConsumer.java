package com.jenu.event.listener;

import com.jenu.client.BookingClient;
import com.jenu.enums.SeatAvailabilityStatus;
import com.jenu.enums.SeatType;
import com.jenu.event.FlightInstanceCreatedEvent;
import com.jenu.event.PaymentCompletedEvent;
import com.jenu.model.CabinClass;
import com.jenu.model.FlightInstanceCabin;
import com.jenu.model.Seat;
import com.jenu.model.SeatInstance;
import com.jenu.payload.response.BookingResponse;
import com.jenu.payload.response.SeatInstanceResponse;
import com.jenu.repository.CabinClassRepository;
import com.jenu.repository.FlightInstanceCabinRepository;
import com.jenu.repository.SeatInstanceRepository;
import com.jenu.repository.SeatRepository;
import com.jenu.service.SeatInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class FlightInstanceEventConsumer {


    private final SeatInstanceService seatInstanceService;
    private final CabinClassRepository cabinClassRepository;
    private final SeatRepository seatRepository;
    private final SeatInstanceRepository seatInstanceRepository;
    private final FlightInstanceCabinRepository flightInstanceCabinRepository;

    @KafkaListener(topics = "flight.instance.created", groupId = "seat-service-group")
    public void handleFlightInstanceCreated(FlightInstanceCreatedEvent event) {

        List<CabinClass> cabin = cabinClassRepository.findByAircraftId(event.getAircraftId());

        int totalSeatInstances = 0;

        for (CabinClass cabinClass : cabin) {
            List<Seat> seats = cabinClass.getSeatMap() != null
                    ? seatRepository.findBySeatMapId(cabinClass.getSeatMap().getId())
                    : List.of();

            FlightInstanceCabin flightInstanceCabin = FlightInstanceCabin
                    .builder()
                    .flightInstanceId(event.getFlightInstanceId())
                    .totalSeats(seats.size())
                    .bookedSeats(0)
                    .cabinClass(cabinClass)
                    .build();
            FlightInstanceCabin savedFlightInstanceCabin = flightInstanceCabinRepository.save(flightInstanceCabin);

            List<SeatInstance> seatInstances = seats
                    .stream()
                    .map(seat -> SeatInstance.builder()
                            .flightId(event.getFlightId())
                            .flightInstanceId(event.getFlightInstanceId())
                            .flightInstanceCabin(flightInstanceCabin)
                            .seat(seat)
                            .status(SeatAvailabilityStatus.AVAILABLE)
                            .isAvailable(true)
                            .isBooked(false)
                            .premiumSurcharge(getPremiumSurcharge(seat.getSeatType(),1000.0,500.0))
                            .build())
                    .toList();

            seatInstanceRepository.saveAll(seatInstances);
            totalSeatInstances+= seatInstances.size();
        }
    }


    private Double getPremiumSurcharge(SeatType seatType,
                                       Double windowSurcharge,
                                       Double aisleSurcharge) {
        if (seatType == null) return 0.0;

        return switch (seatType) {
            case WINDOW -> windowSurcharge != null ? windowSurcharge : 0.0;
            case AISLE -> aisleSurcharge != null ? aisleSurcharge : 0.0;
            default -> 0.0;
        };
    }

}
