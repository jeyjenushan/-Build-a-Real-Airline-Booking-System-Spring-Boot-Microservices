package com.jenu.repository;

import com.jenu.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long>, JpaSpecificationExecutor<Meal> {

    boolean existsByCodeAndAirlineId(String code, Long airlineId);

    List<Meal> findByAirlineId(Long airlineId);
}
