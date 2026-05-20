package com.jenu.service;

import com.jenu.payload.request.BaggagePolicyRequest;
import com.jenu.payload.response.BaggagePolicyResponse;

import java.util.List;

public interface BaggagePolicyService {

    BaggagePolicyResponse createBaggagePolicy(BaggagePolicyRequest request);
    List<BaggagePolicyResponse> createBaggagePolicies(List<BaggagePolicyRequest> requests);
    BaggagePolicyResponse getBaggagePolicyById(Long id);
    BaggagePolicyResponse getBaggagePolicyByFareId(Long fareId);
    List<BaggagePolicyResponse> getBaggagePoliciesByAirlineId(Long airlineId);
    BaggagePolicyResponse updateBaggagePolicy(Long id, BaggagePolicyRequest request);
    void deleteBaggagePolicy(Long id);
}
