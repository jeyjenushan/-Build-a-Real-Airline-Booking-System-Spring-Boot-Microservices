package com.jenu.service.impl;

import com.jenu.mapper.FareMapper;
import com.jenu.model.Fare;
import com.jenu.payload.request.FareRequest;
import com.jenu.payload.response.FareResponse;
import com.jenu.repository.FareRepository;
import com.jenu.service.FareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FareServiceImpl implements FareService {
    private final FareRepository fareRepository;

    @Override
    public FareResponse createFare(FareRequest fareRequest) throws Exception {
        if(fareRepository.existsByFlightIdAndCabinClassIdAndName(fareRequest.getFlightId(), fareRequest.getCabinClassId(), fareRequest.getName())){
            throw new Exception("fare already exist with provided name");
        }
        Fare fare=FareMapper.toEntity(fareRequest);
        Fare savedFare=fareRepository.save(fare);
        return FareMapper.toResponse(savedFare);
    }

    @Override
    public FareResponse getFareById(Long id) throws Exception {
        Fare fare=fareRepository.findById(id)
                .orElseThrow(
                        ()->new Exception("Fare not found with given id")
                );
        return FareMapper.toResponse(fare);

    }

    @Override
    public List<FareResponse> getFaresByFlightIdAndCabinClassId(Long flightId, Long cabinClassId) {
        return fareRepository.findByFlightIdAndCabinClassId(flightId, cabinClassId)
                .stream()
                .map(FareMapper::toResponse).toList();
    }

    @Override
    public FareResponse updateFareById(Long id, FareRequest fareRequest) throws Exception {
        Fare fare=fareRepository.findById(id)
                .orElseThrow(
                        ()->new Exception("Fare not found with given id")
                );
        if(fareRepository.existsByFlightIdAndCabinClassIdAndNameAndIdNot
                (fareRequest.getFlightId(), fareRequest.getCabinClassId(), fareRequest.getName(),id )){
            throw new Exception("fare already exist with provided name");
        }
        FareMapper.updateEntity(fareRequest,fare);
        return FareMapper.toResponse(fareRepository.save(fare));
    }

    @Override
    public void deleteFareById(Long id) throws Exception {
        Fare fare=fareRepository.findById(id)
                .orElseThrow(
                        ()->new Exception("Fare not found with given id")
                );
        fareRepository.delete(fare);

    }

    @Override
    public List<Fare> getFares() {
        return fareRepository.findAll();
    }

    @Override
    public Map<Long, FareResponse> getLowestFarePerFlight(List<Long> flightIds, Long cabinClassId) {
        if(flightIds.size()==0 || flightIds==null) return Map.of();
        List<Fare> fares=fareRepository.findByFlightIdInAndCabinClassId(flightIds, cabinClassId);

        Map<Long,FareResponse> result= fares.stream()
                .collect(Collectors.toMap(
                        Fare::getFlightId,
                        fare -> fare,
                        // merge: keep the fare with the lower total price
                        (existing, candidate) ->
                                candidate.getTotalPrice() < existing.getTotalPrice()
                                        ? candidate : existing
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> FareMapper.toResponse(e.getValue())
                ));


        return result;
    }

    @Override
    public Map<Long, FareResponse> getFaresByIds(List<Long> ids) {
        List<Fare> fares=fareRepository.findAllById(ids);
        return  fares.stream().collect(Collectors.toMap
                (Fare::getId, FareMapper::toResponse));

    }
}
