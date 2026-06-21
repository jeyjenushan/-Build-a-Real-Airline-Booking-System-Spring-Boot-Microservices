package com.jenu.ancillaryservice.service.impl;

import com.jenu.ancillaryservice.mapper.AncillaryMapper;
import com.jenu.ancillaryservice.mapper.InsuranceCoverageMapper;
import com.jenu.ancillaryservice.model.Ancillary;
import com.jenu.ancillaryservice.model.InsuranceCoverage;
import com.jenu.ancillaryservice.repository.AncillaryRepository;
import com.jenu.ancillaryservice.repository.InsuranceCoverageRepository;
import com.jenu.ancillaryservice.service.AncillaryService;
import com.jenu.payload.request.AncillaryRequest;
import com.jenu.payload.response.AncillaryResponse;
import com.jenu.payload.response.InsuranceCoverageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AncillaryServiceImpl implements AncillaryService {

    private final AncillaryRepository ancillaryRepository;
    private final InsuranceCoverageRepository insuranceCoverageRepository;
   // private final AirlineIntegrationService airlineIntegrationService;

    @Override
    public AncillaryResponse create(Long airlineId, AncillaryRequest request) throws Exception {
        //Long airlineId=airlineIntegrationService.getAirlineIdForUser(userId);
        Ancillary ancillary = Ancillary.builder()
                .type(request.getType())
                .subType(request.getSubType())
                .rfisc(request.getRfisc())
                .name(request.getName())
                .description(request.getDescription())
                .metadata(request.getMetadata())
                .displayOrder(request.getDisplayOrder())
                .airlineId(airlineId)
                .build();

        Ancillary saved = ancillaryRepository.save(ancillary);
        return AncillaryMapper.toResponse(saved, null);
    }

    @Override
    public AncillaryResponse getById(Long id) throws Exception {
        Ancillary ancillary = ancillaryRepository.findById(id)
                .orElseThrow(() -> new Exception("Ancillary not found with id: " + id));

        List<InsuranceCoverage> insuranceCoverages = insuranceCoverageRepository.findByAncillary(ancillary);
        List<InsuranceCoverageResponse> coverageResponseList = insuranceCoverages.stream()
                .map(InsuranceCoverageMapper::toResponse)
                .toList();

        return AncillaryMapper.toResponse(ancillary, coverageResponseList);
    }

    @Override
    public List<AncillaryResponse> getAllByAirlineId(Long airlineId) {
      //  Long airlineId=airlineIntegrationService.getAirlineIdForUser(userId);
        return ancillaryRepository.findByAirlineId(airlineId)
                .stream()
                .map(ancillary -> {
                    List<InsuranceCoverage> insuranceCoverages = insuranceCoverageRepository
                            .findByAncillary(ancillary);
                    List<InsuranceCoverageResponse> coverageResponseList = insuranceCoverages.stream()
                            .map(InsuranceCoverageMapper::toResponse)
                            .toList();
                    return AncillaryMapper.toResponse(ancillary, coverageResponseList);
                })
                .collect(Collectors.toList());
    }

    @Override
    public AncillaryResponse update(Long id, AncillaryRequest request) throws Exception {
        Ancillary ancillary = ancillaryRepository.findById(id)
                .orElseThrow(() -> new Exception("Ancillary not found with id: " + id));

        ancillary.setType(request.getType());
        ancillary.setSubType(request.getSubType());
        ancillary.setRfisc(request.getRfisc());
        ancillary.setName(request.getName());
        ancillary.setDescription(request.getDescription());
        ancillary.setMetadata(request.getMetadata());
        ancillary.setDisplayOrder(request.getDisplayOrder());

        Ancillary updated = ancillaryRepository.save(ancillary);

        List<InsuranceCoverage> insuranceCoverages = insuranceCoverageRepository.findByAncillary(ancillary);
        List<InsuranceCoverageResponse> coverageResponseList = insuranceCoverages.stream()
                .map(InsuranceCoverageMapper::toResponse)
                .toList();

        return AncillaryMapper.toResponse(updated, coverageResponseList);
    }

    @Override
    public void delete(Long id) {
        ancillaryRepository.deleteById(id);
    }
}
