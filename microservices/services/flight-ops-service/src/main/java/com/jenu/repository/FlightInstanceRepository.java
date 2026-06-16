package com.jenu.repository;

import com.jenu.model.FlightInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface FlightInstanceRepository extends JpaRepository<FlightInstance, Long> {

    @Query("""
select fi from FlightInstance fi
where fi.airlineId =:airlineId
and (:departureAirportId is null or fi.departureAirportId =:departureAirportId)
or (:arrivalAirportId is null or fi.arrivalAirportId =:arrivalAirportId)
and (:flightId is null or fi.flight.id =:flightId)
and (:dayStart is null or fi.departureDateTime >= :dayStart)
and (:dayEnd is null or fi.arrivalDateTime <= :dayEnd)
""")
    Page<FlightInstance> findByAirlineId(@Param("airlineId") Long airlineId
            , @Param("departureAirportId") Long departureAirportId,
                                         @Param("arrivalAirportId") Long arrivalAirportId,
                                         @Param("flightId") Long flightId,
                                         @Param("dayStart")LocalDateTime dayStart,
                                         @Param("dayEnd")LocalDateTime dayEnd,
                                         Pageable pageable
                                         );


    @Query("SELECT fi FROM FlightInstance fi JOIN FETCH fi.flight WHERE fi.id IN :ids")
    List<FlightInstance> findAllByIdInWithFlight(@Param("ids") Collection<Long> ids);





}
