package com.jenu.service;


import com.jenu.model.Passenger;
import com.jenu.payload.request.PassengerRequest;

public interface PassengerService {


    Passenger findOrCreatePassengerEntity(PassengerRequest request, Long userId);

}
