package com.jenu.repository;

import com.jenu.model.Ancillary;
import com.jenu.model.InsuranceCoverage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceCoverageRepository extends JpaRepository<InsuranceCoverage, Long> {

    List<InsuranceCoverage> findByAncillary(Ancillary ancillary);


    List<InsuranceCoverage> findByAncillaryIdAndActiveTrue(Long ancillaryId);
}
