package com.jenu.repository;

import com.jenu.model.FareRules;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Repository
public interface FareRulesRepository extends JpaRepository<FareRules, Long> {
    boolean existsByFareId( Long fareId);


    Optional<FareRules> findByFareId(Long fareId);

    List<FareRules> findByAirlineId(Long airlineId);
}
