package com.jenu.bookingservice.repository;



import com.jenu.bookingservice.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingPerformanceRepository {

    // Booking Statistics
    Long countBookingsByFlightIdAndDateRange(Long flightId, LocalDateTime startDate, LocalDateTime endDate);

    Double sumRevenueByFlightIdAndDateRange(Long flightId, LocalDateTime startDate, LocalDateTime endDate);

    List<Booking> findBookingsByFlightIdAndDateRange(
            Long flightId, LocalDateTime startDate, LocalDateTime endDate);
}
