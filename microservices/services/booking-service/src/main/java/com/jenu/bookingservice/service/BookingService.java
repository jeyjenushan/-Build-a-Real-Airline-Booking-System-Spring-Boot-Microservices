package com.jenu.bookingservice.service;

import com.jenu.enums.BookingStatus;
import com.jenu.exception.PaymentException;
import com.jenu.exception.ResourceNotFoundException;
import com.jenu.payload.request.BookingRequest;
import com.jenu.payload.response.BookingResponse;
import com.jenu.payload.response.BookingStatisticsResponse;
import com.jenu.payload.response.PaymentInitiateResponse;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request, Long userId)
            throws ResourceNotFoundException, PaymentException;

    BookingResponse updateBooking(Long id, BookingRequest request)
            throws ResourceNotFoundException;

    BookingResponse getBookingById(Long id) throws ResourceNotFoundException;



    List<BookingResponse> getBookingsByAirline(
            Long userId,
            String searchQuery,
            BookingStatus status,
            Long flightInstanceId,
            String sortDirection
    );

    List<BookingResponse> getBookingsByUser(Long userId);

    BookingResponse cancelBooking(Long id) throws ResourceNotFoundException;

    void deleteBooking(Long id) throws ResourceNotFoundException;

    boolean existsById(Long id);

    long count();

    long countByFlightId(Long flightId);

    BookingStatisticsResponse getBookingStatisticsForAirline(Long airlineId);
}
