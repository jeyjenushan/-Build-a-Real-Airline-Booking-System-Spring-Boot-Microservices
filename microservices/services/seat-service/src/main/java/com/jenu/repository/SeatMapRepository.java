package com.jenu.repository;

import com.jenu.model.SeatMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatMapRepository extends JpaRepository<SeatMap, Long> {

    SeatMap findByCabinClassId(Long cabinClassId);
    boolean existsByAirlineIdAndCabinClassIdAndName(Long airlineId, Long cabinClassId, String name);
    boolean existsByAirlineIdAndNameAndIdNot(Long airlineId, String name, Long id);


    @Query("SELECT sm FROM SeatMap sm LEFT JOIN FETCH sm.cabinClass WHERE sm.id = :id")
    Optional<SeatMap> findByIdWithDetails(@Param("id") Long id);



}
