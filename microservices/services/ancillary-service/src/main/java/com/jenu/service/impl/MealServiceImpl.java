package com.jenu.service.impl;

import com.jenu.client.AirlineClient;
import com.jenu.mapper.MealMapper;
import com.jenu.model.Meal;
import com.jenu.repository.MealRepository;
import com.jenu.service.MealService;
import com.jenu.exception.ResourceNotFoundException;
import com.jenu.payload.request.MealRequest;
import com.jenu.payload.response.AirlineResponse;
import com.jenu.payload.response.MealResponse;

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
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
  private final AirlineClient airlineClient;


    @Override
    @Transactional
    public MealResponse create(Long userId, MealRequest request) throws ResourceNotFoundException {
        log.debug("Creating meal with code: {}", request.getCode());

        AirlineResponse airlineResponse=airlineClient.getAirlineByOwner(userId);

        //Specification<Meal> spec = MealSpecification.hasCodeAndAirlineId(request.getCode(), airlineId);
       /* if (mealRepository.exists(spec)) {
            throw new IllegalArgumentException(
                    "Meal with code " + request.getCode() + " already exists for this airline");
        }*/

        if(mealRepository.existsByCodeAndAirlineId(request.getCode(),airlineResponse.getId())){
            throw new IllegalArgumentException("Meal code already exist");
        }

        Meal meal = Meal.builder()
                .code(request.getCode())
                .name(request.getName())
                .mealType(request.getMealType())
                .dietaryRestriction(request.getDietaryRestriction())
                .ingredients(request.getIngredients())
                .imageUrl(request.getImageUrl())
                .available(request.getAvailable())
                .requiresAdvanceBooking(request.getRequiresAdvanceBooking() != null
                        ? request.getRequiresAdvanceBooking() : false)
                .advanceBookingHours(request.getAdvanceBookingHours())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .airlineId(airlineResponse.getId())
                .build();

        Meal savedMeal = mealRepository.save(meal);
        log.info("Meal created successfully with id: {}", savedMeal.getId());
        return MealMapper.toResponse(savedMeal);
    }

    @Override
    @Transactional
    public List<MealResponse> bulkCreate(Long userId, List<MealRequest> requests)
            throws ResourceNotFoundException {
        log.debug("Bulk creating {} meals", requests.size());

        AirlineResponse airlineResponse=airlineClient.getAirlineByOwner(userId);

        List<MealResponse> responses = new ArrayList<>();

        for (MealRequest request : requests) {
           /* Specification<Meal> spec = MealSpecification.hasCodeAndAirlineId(request.getCode(), airlineId);
            if (mealRepository.exists(spec)) {
                log.warn("Skipping meal with code {} - already exists for airline {}",
                        request.getCode(), airlineId);
                continue;
            }*/

            Meal meal = Meal.builder()
                    .code(request.getCode())
                    .name(request.getName())
                    .mealType(request.getMealType())
                    .dietaryRestriction(request.getDietaryRestriction())
                    .ingredients(request.getIngredients())
                    .imageUrl(request.getImageUrl())
                    .available(request.getAvailable())
                    .requiresAdvanceBooking(request.getRequiresAdvanceBooking() != null
                            ? request.getRequiresAdvanceBooking() : false)
                    .advanceBookingHours(request.getAdvanceBookingHours())
                    .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                    .airlineId(airlineResponse.getId())
                    .build();

            Meal savedMeal = mealRepository.save(meal);
            responses.add(MealMapper.toResponse(savedMeal));
        }

        log.info("Successfully created {} meals", responses.size());
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public MealResponse getById(Long id) throws ResourceNotFoundException {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + id));
        return MealMapper.toResponse(meal);
    }




    @Override
    @Transactional(readOnly = true)
    public List<MealResponse> getByAirlineId(Long userId) {
        AirlineResponse airlineResponse=airlineClient.getAirlineByOwner(userId);
        //Specification<Meal> spec = MealSpecification.hasAirlineId(airlineId);
        /*return mealRepository.findAll(spec).stream()
                .map(MealMapper::toResponse)
                .collect(Collectors.toList());*/
        return mealRepository.findByAirlineId(airlineResponse.getId())
                .stream().map(
                        MealMapper::toResponse
                ).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public MealResponse update(Long userId, Long id, MealRequest request) throws ResourceNotFoundException {
        log.debug("Updating meal with id: {}", id);

        AirlineResponse airlineResponse=airlineClient.getAirlineByOwner(userId);

        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + id));

        if (!meal.getCode().equals(request.getCode())) {
          /* Specification<Meal> spec = MealSpecification.hasCodeAndAirlineId(request.getCode(), airlineId);
            if (mealRepository.exists(spec)) {
                throw new IllegalArgumentException(
                        "Meal with code " + request.getCode() + " already exists for this airline");
            }*/
            if(mealRepository.existsByCodeAndAirlineId(request.getCode(),airlineResponse.getId())){
                throw new IllegalArgumentException("Meal code already exist");
            }

        }

        meal.setCode(request.getCode());
        meal.setName(request.getName());
        meal.setMealType(request.getMealType());
        meal.setDietaryRestriction(request.getDietaryRestriction());
        meal.setIngredients(request.getIngredients());
        meal.setImageUrl(request.getImageUrl());
        meal.setAvailable(request.getAvailable());
        meal.setRequiresAdvanceBooking(request.getRequiresAdvanceBooking());
        meal.setAdvanceBookingHours(request.getAdvanceBookingHours());
        meal.setDisplayOrder(request.getDisplayOrder());

        Meal updatedMeal = mealRepository.save(meal);
        log.info("Meal updated successfully with id: {}", updatedMeal.getId());
        return MealMapper.toResponse(updatedMeal);
    }

    @Override
    @Transactional
    public void delete(Long id) throws ResourceNotFoundException {
        if (!mealRepository.existsById(id)) {
            throw new ResourceNotFoundException("Meal not found with id: " + id);
        }
        mealRepository.deleteById(id);
        log.info("Meal deleted successfully with id: {}", id);
    }

    @Override
    @Transactional
    public MealResponse updateAvailability(Long id, Boolean available) throws ResourceNotFoundException {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + id));
        meal.setAvailable(available);
        Meal updatedMeal = mealRepository.save(meal);
        log.info("Meal availability updated successfully for id: {}", updatedMeal.getId());
        return MealMapper.toResponse(updatedMeal);
    }
}
