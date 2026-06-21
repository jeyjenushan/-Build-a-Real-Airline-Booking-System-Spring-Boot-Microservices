package com.jenu.ancillaryservice.repository;

import com.jenu.ancillaryservice.model.FlightMeal;
import com.jenu.ancillaryservice.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface FlightMealRepository extends JpaRepository<FlightMeal, Long>, JpaSpecificationExecutor<FlightMeal> {
    Optional<FlightMeal> findByFlightIdAndMeal(Long flightId, Meal meal);

    List<FlightMeal> findByFlightId(Long flightId);
}
