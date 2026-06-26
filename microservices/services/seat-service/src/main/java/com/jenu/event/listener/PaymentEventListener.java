package com.jenu.event.listener;
import com.jenu.client.BookingClient;
import com.jenu.enums.SeatAvailabilityStatus;
import com.jenu.event.PaymentCompletedEvent;
import com.jenu.payload.response.BookingResponse;
import com.jenu.payload.response.SeatInstanceResponse;
import com.jenu.service.SeatInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {


    private final BookingClient bookingClient;
    private final SeatInstanceService seatInstanceService;

    @KafkaListener(topics = "payment.completed", groupId = "seat-service-group")
    public void handleBookingConfirmed(PaymentCompletedEvent event){
        BookingResponse bookingResponse=bookingClient.getBookingById(event.getBookingId());

        List<SeatInstanceResponse> seatInstanceIds=bookingResponse.getSeatInstances();

        for(SeatInstanceResponse seatInstance: seatInstanceIds){
            seatInstanceService.updateSeatInstanceStatus(seatInstance.getId(),  SeatAvailabilityStatus.BOOKED);
        }
    }

}
