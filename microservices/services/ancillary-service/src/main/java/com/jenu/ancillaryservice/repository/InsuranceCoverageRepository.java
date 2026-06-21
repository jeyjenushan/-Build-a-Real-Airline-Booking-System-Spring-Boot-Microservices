package com.jenu.ancillaryservice.repository;

import com.jenu.ancillaryservice.model.Ancillary;
import com.jenu.ancillaryservice.model.InsuranceCoverage;
import com.jenu.enums.CoverageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceCoverageRepository extends JpaRepository<InsuranceCoverage, Long> {

    List<InsuranceCoverage> findByAncillary(Ancillary ancillary);


    List<InsuranceCoverage> findByAncillaryIdAndActiveTrue(Long ancillaryId);
}
