package com.jenu.service;

import com.jenu.payload.request.FareRulesRequest;
import com.jenu.payload.response.FareRulesResponse;

import java.util.List;

public interface FareRulesService {

    FareRulesResponse createFareRules(FareRulesRequest fareRulesRequest);
    FareRulesResponse getFareRulesById(Long id);
    FareRulesResponse getFareRulesByFareId(Long fareId);
    List<FareRulesResponse> getAllFareRulesByAirlineId(Long airlineId);
    FareRulesResponse updateFareRules(Long id,FareRulesRequest fareRulesRequest);
    void deleteFareRules(Long id);

}
