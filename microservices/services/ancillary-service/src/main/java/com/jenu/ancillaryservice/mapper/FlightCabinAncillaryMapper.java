package com.jenu.ancillaryservice.mapper;


import com.jenu.ancillaryservice.model.FlightCabinAncillary;
import com.jenu.payload.response.FlightCabinAncillaryResponse;
import com.jenu.payload.response.InsuranceCoverageResponse;

import java.util.List;

public class FlightCabinAncillaryMapper {



    public static FlightCabinAncillaryResponse toResponse(
            FlightCabinAncillary entity,
            List<InsuranceCoverageResponse> coverages) {
        if (entity == null) {
            return null;
        }

        return FlightCabinAncillaryResponse.builder()
                .id(entity.getId())
                .flightId(entity.getFlightId())
                .cabinClassId(entity.getCabinClassId())
                .ancillary(AncillaryMapper.toResponse(entity.getAncillary(), coverages))
                .available(entity.getAvailable())
                .maxQuantity(entity.getMaxQuantity())
                .price(entity.getPrice())
                .currency(entity.getCurrency())
                .includedInFare(entity.getIncludedInFare())
                .build();
    }
}
