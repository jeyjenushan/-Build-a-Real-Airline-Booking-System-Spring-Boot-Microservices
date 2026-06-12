package com.jenu.model;

import com.jenu.enums.AirCraftStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "aircrafts")
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Aircraft code is required")
    @Column(nullable = false, unique = true, name = "aircraft_code", length = 20)
    private String code;

    @NotBlank(message = "Aircraft model is required")
    @Column(nullable = false, name = "model", length = 50)
    private String model;

    @NotBlank(message = "Manufacturer is required")
    @Column(nullable = false, length = 50)
    private String manufacturer;

    @NotNull(message = "Seating capacity is required")
    @Positive(message = "Seating capacity must be positive")
    @Column(nullable = false)
    private Integer seatingCapacity;

    @Column(name = "economy_seats")
    private Integer economySeats;

    @Column(name = "premium_economy_seats")
    private Integer premiumEconomySeats;

    @Column(name = "business_seats")
    private Integer businessSeats;

    @Column(name = "first_class_seats")
    private Integer firstClassSeats;

    @Positive(message = "Range must be positive")
    @Column(name = "range_km")
    private Integer rangeKm;

    @Column(name = "cruising_speed_kmh")
    private Integer cruisingSpeedKmh;

    @Column(name = "max_altitude_ft")
    private Integer maxAltitudeFt;

    @Column(name = "year_of_manufacture")
    private Integer yearOfManufacture;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "next_maintenance_date")
    private LocalDate nextMaintenanceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false,length = 20)
    private AirCraftStatus status=AirCraftStatus.ACTIVE;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable=true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    @NotNull(message = "Airline is required")
    private Airline airline;

    @Column(name = "current_airport_id")
    private Long currentAirportId;

    @CreatedDate
    @Column(name = "created_at",updatable = false,nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name="updated_at",nullable = false)
    private Instant updatedAt;

    public Integer getTotalSeats() {
        return (economySeats != null ? economySeats : 0) +
                (premiumEconomySeats != null ? premiumEconomySeats : 0) +
                (businessSeats != null ? businessSeats : 0) +
                (firstClassSeats != null ? firstClassSeats : 0);
    }

    public boolean isOperational() {
        return AirCraftStatus.ACTIVE.equals(status)
                && Boolean.TRUE.equals(isAvailable);
    }

    public boolean requiresMaintenance() {
        return nextMaintenanceDate != null
                && nextMaintenanceDate.isBefore(
                LocalDate.now().plusWeeks(2));
    }





    
}
