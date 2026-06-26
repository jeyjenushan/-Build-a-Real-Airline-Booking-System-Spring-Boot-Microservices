package com.jenu.client;


import com.jenu.payload.response.BookingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "booking-service", fallback = BookingClientFallback.class)
public interface BookingClient {

    @GetMapping("/api/bookings/{id:\\d+}")
    BookingResponse getBookingById(
            @PathVariable Long id
    ) ;

}
