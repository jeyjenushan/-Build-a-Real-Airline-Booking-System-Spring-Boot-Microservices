package com.jenu.payload.request;

import com.jenu.enums.AirlineStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirlineRequest {

    @NotBlank(message = "IATA code is mandatory")
    @Size(min=2,max = 2,message = "IATA code must be exactly 2 characters")
    private String iataCode;

    @NotBlank(message = "ICAO code is mandatory")
    @Size(min=3,max = 3,message = "ICAO code must be exactly 3 characters")
    private String icaoCode;

    @NotBlank(message = "Airline name is mandatory")
    private String name;

    private String aliance;
    private String alias;
    private String logoUrl;
    @NotBlank
    private String country;
    private AirlineStatus airlineStatus;
    private String website;
    private Long headquartersCityId;
    private String supportEmail;
    private String supportPhone;
    private String supportHours;





}
