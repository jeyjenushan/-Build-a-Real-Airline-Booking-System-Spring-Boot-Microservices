package com.jenu.repository;

import com.jenu.enums.CabinClassType;
import com.jenu.model.FlightInstanceCabin;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FlightInstanceCabinRepository extends JpaRepository<FlightInstanceCabin, Long> {
    Page<FlightInstanceCabin> findByFlightInstanceId(Long flightInstanceId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT fc FROM FlightInstanceCabin fc WHERE fc.id = :id")
    Optional<FlightInstanceCabin> findByIdForUpdate(@Param("id") Long id);

    FlightInstanceCabin findByFlightInstanceIdAndCabinClassId(Long flightInstanceId, Long cabinClassId);
}
