package com.jenu.mapper;

import com.jenu.model.Aircraft;
import com.jenu.model.Airline;
import com.jenu.payload.request.AircraftRequest;
import com.jenu.payload.response.AircraftResponse;

public class AircraftMapper {

    public static Aircraft convertToAircraft(AircraftRequest aircraftRequest, Airline airline){
        if(aircraftRequest == null)return null;
        return Aircraft.builder()
                .code(aircraftRequest.getCode())
                .model(aircraftRequest.getModel())
                .manufacturer(aircraftRequest.getManufacturer())
                .seatingCapacity(aircraftRequest.getSeatingCapacity())
                .economySeats(aircraftRequest.getEconomySeats())
                .premiumEconomySeats(aircraftRequest.getPremiumEconomySeats())
                .businessSeats(aircraftRequest.getBusinessSeats())
                .firstClassSeats(aircraftRequest.getFirstClassSeats())
                .rangeKm(aircraftRequest.getRangeKm())
                .cruisingSpeedKmh(aircraftRequest.getCruisingSpeedKmh())
                .maxAltitudeFt(aircraftRequest.getMaxAltitudeFt())
                .yearOfManufacture(aircraftRequest.getYearOfManufacture())
                .registrationDate(aircraftRequest.getRegistrationDate())
                .nextMaintenanceDate(aircraftRequest.getNextMaintenanceDate())
                .status(aircraftRequest.getStatus())
                .isAvailable(aircraftRequest.getIsAvailable())
                .airline(airline)
                .currentAirportId(aircraftRequest.getCurrentAirportId())
                .build();

    }

    public static AircraftResponse convertToAircraftResponse(Aircraft aircraft){
        if(aircraft == null)return null;
        return AircraftResponse.builder()
                .id(aircraft.getId())
                .code(aircraft.getCode())
                .model(aircraft.getModel())
                .manufacturer(aircraft.getManufacturer())
                .seatingCapacity(aircraft.getSeatingCapacity())
                .economySeats(aircraft.getEconomySeats())
                .premiumEconomySeats(aircraft.getPremiumEconomySeats())
                .businessSeats(aircraft.getBusinessSeats())
                .firstClassSeats(aircraft.getFirstClassSeats())
                .rangeKm(aircraft.getRangeKm())
                .cruisingSpeedKmh(aircraft.getCruisingSpeedKmh())
                .maxAltitudeFt(aircraft.getMaxAltitudeFt())
                .yearOfManufacture(aircraft.getYearOfManufacture())
                .registrationDate(aircraft.getRegistrationDate())
                .nextMaintenanceDate(aircraft.getNextMaintenanceDate())
                .status(aircraft.getStatus())
                .isAvailable(aircraft.getIsAvailable())
                .airlineId(aircraft.getAirline()!=null?aircraft.getAirline().getId():null)
                .airlineName(aircraft.getAirline()!=null?aircraft.getAirline().getName():null)
                .airlineIataCode(aircraft.getAirline()!=null?aircraft.getAirline().getIataCode():null)
                .currentAirportId(aircraft.getCurrentAirportId())
                .totalSeats(aircraft.getTotalSeats())
                .requiresMaintenance(aircraft.requiresMaintenance())
                .isOperational(aircraft.isOperational())
                .createdAt(aircraft.getCreatedAt())
                .updatedAt(aircraft.getUpdatedAt())
                .build();

    }

    public static void updateEntity(Aircraft aircraft,AircraftRequest aircraftRequest){
        if(aircraftRequest == null || aircraft==null)return;

        aircraft.setCode(aircraftRequest.getCode());
        aircraft.setModel(aircraftRequest.getModel());
        aircraft.setManufacturer(aircraftRequest.getManufacturer());
        aircraft.setSeatingCapacity(aircraftRequest.getSeatingCapacity());
        aircraft.setEconomySeats(aircraftRequest.getEconomySeats());
        aircraft.setPremiumEconomySeats(aircraftRequest.getPremiumEconomySeats());
        aircraft.setBusinessSeats(aircraftRequest.getBusinessSeats());
        aircraft.setFirstClassSeats(aircraftRequest.getFirstClassSeats());
        aircraft.setRangeKm(aircraftRequest.getRangeKm());
        aircraft.setCruisingSpeedKmh(aircraftRequest.getCruisingSpeedKmh());
        aircraft.setMaxAltitudeFt(aircraftRequest.getMaxAltitudeFt());
        aircraft.setYearOfManufacture(aircraftRequest.getYearOfManufacture());
        aircraft.setRegistrationDate(aircraftRequest.getRegistrationDate());
        aircraft.setNextMaintenanceDate(aircraftRequest.getNextMaintenanceDate());
        aircraft.setStatus(aircraftRequest.getStatus());
        aircraft.setIsAvailable(aircraftRequest.getIsAvailable());
        aircraft.setCurrentAirportId(aircraftRequest.getCurrentAirportId());

    }
}
