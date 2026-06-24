package com.jenu.bookingservice.service;


import com.jenu.bookingservice.model.Passenger;
import com.jenu.exception.ResourceNotFoundException;
import com.jenu.payload.request.PassengerRequest;
import com.jenu.payload.response.PassengerResponse;

public interface PassengerService {


    Passenger findOrCreatePassengerEntity(PassengerRequest request, Long userId);

}
