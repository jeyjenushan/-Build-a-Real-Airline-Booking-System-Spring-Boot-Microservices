package com.jenu.client;

import com.jenu.payload.response.BookingResponse;
import org.springframework.stereotype.Service;

@Service
public class BookingClientFallback implements BookingClient{
    @Override
    public BookingResponse getBookingById(Long id)  {
        return null;
    }
}
