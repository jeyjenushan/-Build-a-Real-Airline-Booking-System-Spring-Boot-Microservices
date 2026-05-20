package com.jenu.service.impl;

import com.jenu.mapper.FareRulesMapper;
import com.jenu.model.Fare;
import com.jenu.model.FareRules;
import com.jenu.payload.request.FareRulesRequest;
import com.jenu.payload.response.FareRulesResponse;
import com.jenu.repository.FareRepository;
import com.jenu.repository.FareRulesRepository;
import com.jenu.service.FareRulesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FareRulesServiceImpl implements FareRulesService {

    private final FareRulesRepository fareRulesRepository;
    private final FareRepository fareRepository;
    @Override
    public FareRulesResponse createFareRules(FareRulesRequest fareRulesRequest) {
        Fare fare = fareRepository.findById(fareRulesRequest.getFareId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fare not found with id: " + fareRulesRequest.getFareId()));

        if (fareRulesRepository.existsByFareId(fareRulesRequest.getFareId())) {
            throw new IllegalArgumentException(
                    "Fare rules already exist for fare id: " + fareRulesRequest.getFareId());
        }

        FareRules fareRules = FareRulesMapper.toEntity(fareRulesRequest, fare);
        FareRules saved = fareRulesRepository.save(fareRules);
        return FareRulesMapper.toResponse(saved);
    }

    @Override
    public FareRulesResponse getFareRulesById(Long id) {
        FareRules fareRules = fareRulesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fare rules not found with id: " + id));
        return FareRulesMapper.toResponse(fareRules);
    }

    @Override
    public FareRulesResponse getFareRulesByFareId(Long fareId) {
        FareRules fareRules = fareRulesRepository.findByFareId(fareId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fare rules not found for fare id: " + fareId));
        return FareRulesMapper.toResponse(fareRules);
    }

    @Override
    public List<FareRulesResponse> getAllFareRulesByAirlineId(Long airlineId) {
        return fareRulesRepository.findByAirlineId(airlineId).stream()
                .map(FareRulesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FareRulesResponse updateFareRules(Long id, FareRulesRequest fareRulesRequest) {
        FareRules existing = fareRulesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fare rules not found with id: " + id));

        FareRulesMapper.updateEntity(fareRulesRequest, existing);
        FareRules saved = fareRulesRepository.save(existing);
        return FareRulesMapper.toResponse(saved);
    }

    @Override
    public void deleteFareRules(Long id) {
        FareRules fareRules = fareRulesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fare rules not found with id: " + id));
        fareRulesRepository.delete(fareRules);

    }
}
