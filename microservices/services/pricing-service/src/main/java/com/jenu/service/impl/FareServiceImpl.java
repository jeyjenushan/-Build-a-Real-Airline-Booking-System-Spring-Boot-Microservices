package com.jenu.service.impl;

import com.jenu.mapper.FareMapper;
import com.jenu.model.Fare;
import com.jenu.payload.request.FareRequest;
import com.jenu.payload.response.FareResponse;
import com.jenu.repository.FareRepository;
import com.jenu.service.FareService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FareServiceImpl implements FareService {
    private final FareRepository fareRepository;

    @Override
    public FareResponse createFare(FareRequest fareRequest)  {
        if(fareRepository.existsByFlightIdAndCabinClassIdAndName(fareRequest.getFlightId(), fareRequest.getCabinClassId(), fareRequest.getName())){
            throw new EntityNotFoundException("Fare '" + fareRequest.getName() + "' already exists for this flight and cabin class");
        }
        Fare fare=FareMapper.toEntity(fareRequest);
        Fare savedFare=fareRepository.save(fare);
        return FareMapper.toResponse(savedFare);
    }

    @Override
    public List<FareResponse> createFares(List<FareRequest> requests) {
        // Single DB call: fetch composite keys for all relevant flightIds
        Set<Long> flightIds = requests.stream()
                .map(FareRequest::getFlightId)
                .collect(Collectors.toSet());
        Set<String> existingKeys = fareRepository.findExistingFareKeys(flightIds);

        List<Fare> toSave = requests.stream()
                .filter(req -> !existingKeys.contains(
                        req.getFlightId() + ":" + req.getCabinClassId() + ":" + req.getName()))
                .map(FareMapper::toEntity)
                .collect(Collectors.toList());

        return fareRepository.saveAll(toSave).stream()
                .map(FareMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FareResponse getFareById(Long id)  {
        Fare fare=fareRepository.findById(id)
                .orElseThrow(
                        ()->new EntityNotFoundException("Fare not found with given id")
                );
        return FareMapper.toResponse(fare);

    }

    @Override
    @Transactional(readOnly = true)
    public List<FareResponse> getFaresByFlightIdAndCabinClassId(Long flightId, Long cabinClassId) {
        return fareRepository.findByFlightIdAndCabinClassId(flightId, cabinClassId)
                .stream()
                .map(FareMapper::toResponse).toList();
    }

    @Override
    public FareResponse updateFareById(Long id, FareRequest fareRequest)   {
        Fare fare=fareRepository.findById(id)
                .orElseThrow(
                        ()->new EntityNotFoundException("Fare not found with given id")
                );
        if(fareRepository.existsByFlightIdAndCabinClassIdAndNameAndIdNot
                (fareRequest.getFlightId(), fareRequest.getCabinClassId(), fareRequest.getName(),id )){
            throw new EntityNotFoundException("fare already exist with provided name");
        }
        FareMapper.updateEntity(fareRequest,fare);
        return FareMapper.toResponse(fareRepository.save(fare));
    }

    @Override
    public void deleteFareById(Long id)  {
        Fare fare=fareRepository.findById(id)
                .orElseThrow(
                        ()->new IllegalArgumentException("Fare not found with given id")
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
    public FareResponse getLowestFareForFlightAndCabin(Long flightId, Long cabinClassId) {
        List<Fare> fares = fareRepository.findByFlightIdAndCabinClassId(
                flightId,
                cabinClassId
        );

        Fare lowestFare = fares.stream()
                .min(Comparator.comparingDouble(Fare::getTotalPrice))
                .orElse(null);

        return FareMapper.toResponse(lowestFare);
    }

    @Override
    public Map<Long, FareResponse> getFaresByIds(List<Long> ids) {
        List<Fare> fares=fareRepository.findAllById(ids);
        return  fares.stream().collect(Collectors.toMap
                (Fare::getId, FareMapper::toResponse));

    }
}
