package com.jenu.service.impl;

import com.jenu.mapper.FlightMealMapper;
import com.jenu.model.FlightMeal;
import com.jenu.model.Meal;
import com.jenu.repository.FlightMealRepository;
import com.jenu.repository.MealRepository;
import com.jenu.service.FlightMealService;
import com.jenu.payload.request.FlightMealRequest;
import com.jenu.payload.response.FlightMealResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightMealServiceImpl implements FlightMealService {

    private final FlightMealRepository flightMealRepository;
    private final MealRepository mealRepository;

    @Override
    @Transactional
    public FlightMealResponse create(FlightMealRequest request) throws Exception {
        log.debug("Creating flight meal for flight ID: {} and meal ID: {}",
                request.getFlightId(), request.getMealId());

        Meal meal = mealRepository.findById(request.getMealId())
                .orElseThrow(() -> new Exception(
                        "Meal not found with id: " + request.getMealId()));

       /* Specification<FlightMeal> spec = FlightMealSpecification.hasFlightIdAndMealId(
                request.getFlightId(), request.getMealId());
        if (flightMealRepository.exists(spec)) {
            throw new IllegalArgumentException("Meal is already assigned to this flight");
        }*/
        if(flightMealRepository.findByFlightIdAndMeal( request.getFlightId(), meal).isPresent()){
            throw new IllegalArgumentException("Meal is already assigned to this flight");
        }

        FlightMeal flightMeal = FlightMeal.builder()
                .flightId(request.getFlightId())
                .meal(meal)
                .available(request.getAvailable())
                .price(request.getPrice())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();

        FlightMeal saved = flightMealRepository.save(flightMeal);
        log.info("Flight meal created successfully with id: {}", saved.getId());
        return FlightMealMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public List<FlightMealResponse> bulkCreate(List<FlightMealRequest> requests)
            throws Exception {
        log.debug("Bulk creating {} flight meals", requests.size());

        List<FlightMealResponse> responses = new ArrayList<>();

        for (FlightMealRequest request : requests) {
            Meal meal = mealRepository.findById(request.getMealId())
                    .orElseThrow(() -> new Exception(
                            "Meal not found with id: " + request.getMealId()));

            /*Specification<FlightMeal> spec = FlightMealSpecification.hasFlightIdAndMealId(
                    request.getFlightId(), request.getMealId());
            if (flightMealRepository.exists(spec)) {
                log.warn("Skipping flight meal - meal {} already assigned to flight {}",
                        request.getMealId(), request.getFlightId());
                continue;
            }*/
            if(flightMealRepository.findByFlightIdAndMeal( request.getFlightId(), meal).isPresent()){
                throw new IllegalArgumentException("Meal is already assigned to this flight");
            }

            FlightMeal flightMeal = FlightMeal.builder()
                    .flightId(request.getFlightId())
                    .meal(meal)
                    .available(request.getAvailable())
                    .price(request.getPrice())
                    .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                    .build();

            FlightMeal saved = flightMealRepository.save(flightMeal);
            responses.add(FlightMealMapper.toResponse(saved));
        }

        log.info("Successfully created {} flight meals", responses.size());
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public FlightMealResponse getById(Long id) throws Exception {
        FlightMeal flightMeal = flightMealRepository.findById(id)
                .orElseThrow(() -> new Exception("FlightMeal not found with id: " + id));
        return FlightMealMapper.toResponse(flightMeal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightMealResponse> getByFlightId(Long flightId) {
      /*  Specification<FlightMeal> spec = FlightMealSpecification.hasFlightId(flightId);
        return flightMealRepository.findAll(spec).stream()
                .map(FlightMealMapper::toResponse)
                .collect(Collectors.toList());


       */
        return flightMealRepository.findByFlightId(flightId).stream()
                .map(FlightMealMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightMealResponse> getAllByIds(List<Long> Ids) {
        List<FlightMeal> meals = flightMealRepository.findAllById(Ids);
        return meals.stream().map(
                FlightMealMapper::toResponse
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FlightMealResponse update(Long id, FlightMealRequest request) throws Exception {
        log.debug("Updating flight meal with id: {}", id);

        FlightMeal flightMeal = flightMealRepository.findById(id)
                .orElseThrow(() -> new Exception("FlightMeal not found with id: " + id));

        // Update flight ID if changed
        if (!flightMeal.getFlightId().equals(request.getFlightId())) {
            flightMeal.setFlightId(request.getFlightId());
        }

        // Update meal if changed
        if (!flightMeal.getMeal().getId().equals(request.getMealId())) {
            Meal meal = mealRepository.findById(request.getMealId())
                    .orElseThrow(() -> new Exception(
                            "Meal not found with id: " + request.getMealId()));
            flightMeal.setMeal(meal);
        }

        flightMeal.setAvailable(request.getAvailable());
        flightMeal.setPrice(request.getPrice());
        flightMeal.setDisplayOrder(request.getDisplayOrder());

        FlightMeal updated = flightMealRepository.save(flightMeal);
        log.info("Flight meal updated successfully with id: {}", updated.getId());
        return FlightMealMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) throws Exception {
        if (!flightMealRepository.existsById(id)) {
            throw new Exception("FlightMeal not found with id: " + id);
        }
        flightMealRepository.deleteById(id);
        log.info("Flight meal deleted successfully with id: {}", id);
    }

    @Override
    @Transactional
    public FlightMealResponse updateAvailability(Long id, Boolean available) throws Exception {
        FlightMeal flightMeal = flightMealRepository.findById(id)
                .orElseThrow(() -> new Exception("FlightMeal not found with id: " + id));
        flightMeal.setAvailable(available);
        FlightMeal updated = flightMealRepository.save(flightMeal);
        log.info("Flight meal availability updated successfully for id: {}", updated.getId());
        return FlightMealMapper.toResponse(updated);
    }

    @Override
    public Double calculateMealPrice(List<Long> mealIds) {
        List<FlightMeal> meals=flightMealRepository.findAllById(mealIds);
        double total=0.0;
        for(FlightMeal flightMeal : meals) {
            total+=flightMeal.getPrice();
        }
        return total;
    }


}
