package com.jenu.model;

import com.jenu.enums.AirCraftStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String model;

    @Column(nullable = false, length = 50)
    private String manufacturer;

    @Column(nullable = false)
    private int seatingCapacity;

    @Column(name = "economy_seats")
    private int economySeats=0;

    @Column(name = "premium_economy_seats")
    private int premiumEconomySeats=0;

    @Column(name = "business_seats")
    private int businessSeats=0;

    @Column(name = "first_class_seats")
    private int firstClassSeats=0;

    private int rangeKm;

    @Column(name = "cruising_speed_kmh")
    private int cruisingSpeedKmh;

    private int maxAltitudeFt;

    @Column(name = "year_of_manufacture")
    private int yearOfManufacture;

    private LocalDate registrationDate;

    private LocalDate nextMaintenanceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false,length = 20)
    private AirCraftStatus status=AirCraftStatus.ACTIVE;

    private Boolean isAvailable=true;

    @ManyToOne
    private Airline airline;

    private Long currentAirportId;

    @CreatedDate
    @Column(name = "created_at",updatable = false,nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name="updated_at",nullable = false)
    private Instant updatedAt;

    public int getTotalSeats(){
        return economySeats+premiumEconomySeats+businessSeats+firstClassSeats;
    }

    public boolean isOperational(){
        return AirCraftStatus.ACTIVE.equals(status)
                && Boolean.TRUE.equals(isAvailable);
    }

    public boolean requiresMaintenance(){
        return nextMaintenanceDate!=null
                && nextMaintenanceDate.isBefore(LocalDate.now().plusWeeks(2));
    }





    
}
