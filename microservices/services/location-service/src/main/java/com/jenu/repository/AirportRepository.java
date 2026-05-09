package com.jenu.repository;

import com.jenu.model.Airport;
import com.jenu.payload.response.AirportResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    Optional<Airport> findByIataCode(String iataCode);
    List<Airport> findByCityId(Long cityId);
}
