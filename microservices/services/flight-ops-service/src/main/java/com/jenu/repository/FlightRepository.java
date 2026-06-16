package com.jenu.repository;

import com.jenu.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;


@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("""
select f from Flight f
where f.airlineId =:airlineId
and (:depId is null or f.departureAirportId = :depId)
and (:arrId is null or f.arrivalAirportId = :arrId)
""")
    Page<Flight> findByAirlineAndDepartureAirportAndArrivalAirportId(@Param("airlineId") Long airlineId,
                                 @Param("depId") Long depId,
                                 @Param("arrId") Long arrId,
                                 Pageable pageable);
    @Query("SELECT f.flightNumber FROM Flight f WHERE f.flightNumber IN :numbers")
    Set<String> findExistingFlightNumbers(@Param("numbers") Collection<String> numbers);
    Optional<Flight> findByAirlineIdAndId(Long airlineId, Long id);
    boolean existsByFlightNumber(String flightNumber);
    boolean existsByFlightNumberAndIdNot(String flightNumber, Long depId);
    Optional<Flight> findByFlightNumber(String flightNumber);
}
