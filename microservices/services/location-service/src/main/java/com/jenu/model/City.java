package com.jenu.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "cities", indexes = {
        @Index(name = "idx_city_code", columnList = "cityCode"),
        @Index(name = "idx_city_name", columnList = "name"),
        @Index(name = "idx_country_code", columnList = "countryCode")
})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "City name is required")
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "City code is required")
    @Size(max = 10)
    @Column(nullable = false, unique = true)
    private String cityCode;

    @NotBlank(message = "Country code is required")
    @Size(max = 5)
    @Column(nullable = false)
    private String countryCode;

    @NotBlank(message = "Country name is required")
    @Size(max = 100)
    @Column(nullable = false)
    private String countryName;

    @Size(max = 10)
    private String regionCode;

    @Column(name = "time_zone_id",length = 50)
    private String timeZoneId;


}
