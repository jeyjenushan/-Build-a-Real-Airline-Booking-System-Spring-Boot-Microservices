package com.jenu.ancillaryservice.repository;

import com.jenu.ancillaryservice.model.Meal;
import com.jenu.payload.response.MealResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long>, JpaSpecificationExecutor<Meal> {

    boolean existsByCodeAndAirlineId(String code, Long airlineId);

    List<Meal> findByAirlineId(Long airlineId);
}
