package com.jenu.event.listener;

import com.jenu.clients.FlightClient;
import com.jenu.clients.PricingClient;
import com.jenu.clients.UserClient;
import com.jenu.enums.BookingStatus;
import com.jenu.event.PaymentCompletedEvent;
import com.jenu.event.PaymentFailedEvent;
import com.jenu.event.publisher.BookingEventProducer;
import com.jenu.model.Booking;
import com.jenu.payload.dto.UserDto;
import com.jenu.payload.response.FareResponse;
import com.jenu.payload.response.FlightInstanceResponse;
import com.jenu.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final BookingRepository bookingRepository;
    private final FlightClient flightClient;
    private final PricingClient pricingClient;
    private final UserClient userClient;
    private final BookingEventProducer bookingEventProducer;

    @KafkaListener(topics = "payment.completed", groupId = "booking-service-group")
    public void handlePaymentCompleted(PaymentCompletedEvent event){
        Booking booking = bookingRepository.findById(event.getBookingId())
                .orElse(null);
        if (booking == null) {
            log.error("Booking not found for id={}", event.getBookingId());
            return;
        }
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentId(event.getPaymentId());
        booking = bookingRepository.save(booking);
        log.info("Booking {} confirmed after payment {}", booking.getBookingReference(), event.getPaymentId());

        FlightInstanceResponse flightInstanceResponse=flightClient
                .getFlightInstanceById(booking.getFlightInstanceId());

        FareResponse fareResponse = pricingClient.getFareById(booking.getFareId());
        UserDto userDto=userClient.getUserById(booking.getUserId());


        bookingEventProducer.sendBookingConfirmed(booking,event,flightInstanceResponse,fareResponse,userDto);

    }

    @KafkaListener(topics = "payment.failed", groupId = "booking-service-group")
    public void handlePaymentFailed(PaymentFailedEvent event){
        Booking booking = bookingRepository.findById(event.getBookingId())
                .orElse(null);
        if (booking == null) {
            log.error("Booking not found for id={}", event.getBookingId());
            return;
        }
        booking.setStatus(BookingStatus.CANCELLED);
        booking = bookingRepository.save(booking);
        log.info("Booking {} cancelled due to payment failure", booking.getBookingReference());
    }
}
