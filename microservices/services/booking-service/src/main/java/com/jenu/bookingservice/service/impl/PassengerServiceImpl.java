package com.jenu.bookingservice.service.impl;

import com.jenu.bookingservice.mapper.PassengerMapper;
import com.jenu.bookingservice.model.Passenger;
import com.jenu.bookingservice.repository.PassengerRepository;
import com.jenu.bookingservice.service.PassengerService;
import com.jenu.exception.ResourceNotFoundException;
import com.jenu.payload.request.PassengerRequest;
import com.jenu.payload.response.PassengerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;



    @Override
    @Transactional
    public Passenger findOrCreatePassengerEntity(
            PassengerRequest request, Long userId) {
        Optional<Passenger> existing = findExistingPassengerOptional(request);
        if (existing.isPresent()) {
            Passenger passenger = existing.get();
            PassengerMapper.updateEntityFromRequest(request, passenger);
            return passengerRepository.save(passenger);
        }

        Passenger newPassenger = PassengerMapper.toEntity(request);
        newPassenger.setPrimaryUserId(userId);
        return passengerRepository.save(newPassenger);
    }



    private Optional<Passenger> findExistingPassengerOptional(PassengerRequest request) {
        if (request.getPassportNumber() != null && !request.getPassportNumber().isEmpty()) {
            Optional<Passenger> byPassport = passengerRepository.findByPassportNumber(
                    request.getPassportNumber());
            if (byPassport.isPresent()) {
                return byPassport;
            }
        }

        return passengerRepository.findByEmailAndPhoneAndDateOfBirth(
                request.getEmail(), request.getPhone(), request.getDateOfBirth());
    }
}
