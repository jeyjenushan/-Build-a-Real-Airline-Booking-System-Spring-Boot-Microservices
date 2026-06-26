package com.jenu.client;

import com.jenu.enums.CabinClassType;
import com.jenu.payload.response.CabinClassResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SeatClientFallback implements SeatClient {

    @Override
    public List<CabinClassResponse> getCabinClassesByAircraftId(Long aircraftId) {
        return Collections.emptyList();
    }

    @Override
    public CabinClassResponse getCabinClassByAircraftIdAndName(CabinClassType cabinClass, Long id) {
        return null;
    }
}
