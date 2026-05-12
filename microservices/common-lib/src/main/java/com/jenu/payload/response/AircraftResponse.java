package com.jenu.payload.response;

import com.jenu.enums.AirCraftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftResponse {
    private Long id;
    private String code;
    private String model;
    private String manufacturer;
    private int seatingCapacity;
    private int economySeats;
    private int premiumEconomySeats;
    private int businessSeats;
    private int firstClassSeats;
    private int rangeKm;
    private int cruisingSpeedKmh;
    private int maxAltitudeFt;
    private int yearOfManufacture;
    private LocalDate registrationDate;
    private LocalDate nextMaintenanceDate;
    private AirCraftStatus status;
    private Boolean isAvailable;
    private Long airlineId;
    private String airlineName;
    private String airlineIataCode;
    private  Long currentAirportId;
    private Long currentAirportCity;
    private Long currentAirportCode;
    private Long currentAirportName;
    private int totalSeats;
    private Boolean requiresMaintenance;
    private Boolean isOperational;
    private Instant createdAt;
    private Instant updatedAt;


}
