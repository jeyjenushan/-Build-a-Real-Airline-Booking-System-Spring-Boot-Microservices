package com.jenu.repository;

import com.jenu.model.Fare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface FareRepository extends JpaRepository<Fare, Long> {

    boolean existsByFlightIdAndCabinClassIdAndName(Long flightId, Long cabinClassId, String name);
    List<Fare> findByFlightIdAndCabinClassId(Long flightId, Long cabinClassId);
    List<Fare> findByFlightIdInAndCabinClassId(List<Long> flightIds, Long cabinClassId);
    boolean existsByFlightIdAndCabinClassIdAndNameAndIdNot(Long flightId, Long cabinClassId, String name, Long id);
    /**
     * Returns composite keys "flightId:cabinClassId:name" for existing fares
     * whose flightId is in the given collection. Used by bulk create to detect
     * duplicates with a single DB round-trip.
     */
    @Query("SELECT CONCAT(f.flightId, ':', f.cabinClassId, ':', f.name) FROM Fare f WHERE f.flightId IN :flightIds")
    Set<String> findExistingFareKeys(@Param("flightIds") Collection<Long> flightIds);

}
