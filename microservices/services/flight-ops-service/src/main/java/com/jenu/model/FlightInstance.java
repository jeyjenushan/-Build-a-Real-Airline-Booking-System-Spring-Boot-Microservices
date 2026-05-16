package com.jenu.model;

import com.jenu.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
public class FlightInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long airlineId;

    @ManyToOne
    private Flight flight;

    @Column(nullable = false)
    private Long departureAirportId;

    @Column(nullable = false)
    private Long arrivalAirportId;

    @Column(nullable = false)
    private Long scheduleId;

    private LocalDateTime departureDateTime;

    private LocalDateTime arrivalDateTime;

    @Column(nullable = false)
    private int totalSeats;

    @Column(nullable = false)
    private int availableSeats;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightStatus status;

    private int minimumAdvancedBookingDays;

    private int maximumAdvancedBookingDays;

    private Boolean isActive=true;

    @Transient
    public String getFormatedDuration(){
        //5h 45min like this
        if(departureDateTime ==null || arrivalDateTime == null){
            return null;
        }
        Duration duration = Duration.between(departureDateTime, arrivalDateTime);
        long hours = duration.toHours();
        long minutes=duration.toMinutesPart();
        StringBuilder formatedDuration = new StringBuilder();
        if(hours > 0){
            formatedDuration.append(hours).append("h ");
        }
        if(minutes > 0){
            formatedDuration.append(minutes).append("min ");
        }
        return formatedDuration.toString().trim();
    }

}
