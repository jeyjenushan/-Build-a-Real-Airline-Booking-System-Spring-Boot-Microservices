package com.jenu.ancillaryservice.repository;

import com.jenu.ancillaryservice.model.FlightCabinAncillary;
import com.jenu.enums.AncillaryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlightCabinAncillaryRepository extends JpaRepository<FlightCabinAncillary, Long> {


    List<FlightCabinAncillary> findByFlightIdAndCabinClassId(Long flightId, Long cabinClassId);

    Optional<FlightCabinAncillary> findByFlightIdAndCabinClassIdAndAncillary_Type(
            Long flightId, Long cabinClassId, AncillaryType type);

    List<FlightCabinAncillary> findAllByFlightIdAndCabinClassIdAndAncillary_Type(
            Long flightId, Long cabinClassId, AncillaryType type);
}
