package com.jenu.payload.response;

import com.jenu.embeddable.Support;
import com.jenu.enums.AirlineStatus;
import com.jenu.payload.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirlineResponse {
    private Long id;
    private String iataCode;
    private String icaoCode;
    private String name;
    private String alias;
    private String country;
    private String logoUrl;
    private String website;
    private String alliance;
    private AirlineStatus status;



    private Instant createdAt;
    private Instant updatedAt;
    private Long ownerId;
    private UserDto owner;
    private Long updateById;
    private CityResponse headquarterCityId;
    private Support support;


}
