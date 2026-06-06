package com.jenu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jenu.embeddable.Address;
import com.jenu.embeddable.Analytics;
import com.jenu.embeddable.GeoCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZoneId;

@Entity
@Data
@Table(name = "airports", indexes = {
        @Index(name = "idx_airport_iata", columnList = "iataCode"),
        @Index(name = "idx_airport_city_id", columnList = "city_id")
})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "city"})
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(nullable = false, length = 3, unique = true)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String iataCode;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "time_zone_id", length = 50)
    private String timeZoneId;

    @Embedded
    private Address address;

    @Embedded
    private GeoCode geoCode;

    @Embedded
    private Analytics analytics;

    @Column(name = "time_zone",length = 50)
    private String timeZone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonIgnore
    private City city;

    @Transient
    @JsonIgnore
    public ZoneId getTimeZone() {

        return timeZoneId != null ? ZoneId.of(timeZoneId) : null;
    }

    public void setTimeZone(ZoneId zoneId) {
        this.timeZoneId = zoneId != null ? zoneId.getId() : null;
    }

    @JsonIgnore
    @Transient
    public String getDetailedName(){
        if(city != null && city.getCountryCode()!=null){
            return name.toUpperCase() + "/" + city.getCountryCode();

        }
        return name.toUpperCase();
    }


}
