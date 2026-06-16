package com.jenu.model;

import com.jenu.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Table(name = "flight_instances",
        uniqueConstraints = @UniqueConstraint(columnNames = {"flight_id", "departure_date_time"}))
@NoArgsConstructor
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"flight"})
@EqualsAndHashCode(of = "id")
public class FlightInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cross-service ref: Airline is in airline-core-service
    @Column(name = "airline_id")
    private Long airlineId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    // Cross-service ref: Airport is in location-service
    @Column(name = "departure_airport_id", nullable = false)
    private Long departureAirportId;

    // Cross-service ref: Airport is in location-service
    @Column(name = "arrival_airport_id", nullable = false)
    private Long arrivalAirportId;

    @Column(nullable = false)
    private Long scheduleId;

    private LocalDateTime departureDateTime;

    private LocalDateTime arrivalDateTime;

    @Column(nullable = false)
    private int totalSeats;

    @Column(nullable = false)
    private int availableSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FlightStatus status;

    private int minimumAdvancedBookingDays;

    private int maximumAdvancedBookingDays;

    private Boolean isActive=true;

    @Version
    private Long version;

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
