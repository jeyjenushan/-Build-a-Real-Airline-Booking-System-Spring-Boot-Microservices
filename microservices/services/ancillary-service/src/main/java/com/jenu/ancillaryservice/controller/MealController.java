package com.jenu.ancillaryservice.controller;
import com.jenu.ancillaryservice.service.MealService;
import com.jenu.exception.ResourceNotFoundException;
import com.jenu.payload.request.MealRequest;
import com.jenu.payload.response.MealResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @PostMapping
    public ResponseEntity<MealResponse> createMeal(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody MealRequest request) throws Exception {
        MealResponse response = mealService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<MealResponse>> bulkCreateMeals(
            @Valid @RequestBody List<MealRequest> requests,
            @RequestHeader("X-User-Id") Long userId) throws Exception {
        List<MealResponse> responses = mealService.bulkCreate(userId, requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<MealResponse> getMealById(@PathVariable Long id)
            throws Exception {
        return ResponseEntity.ok(mealService.getById(id));
    }

    @GetMapping("/airline")
    public ResponseEntity<List<MealResponse>> getMealsByAirlineId(
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(mealService.getByAirlineId(userId));
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<MealResponse> updateMeal(
            @PathVariable Long id,
            @Valid @RequestBody MealRequest request,
            @RequestHeader("X-User-Id") Long userId) throws Exception {
        return ResponseEntity.ok(mealService.update(userId, id, request));
    }

    @PatchMapping("/{id:\\d+}/availability")
    public ResponseEntity<MealResponse> updateMealAvailability(
            @PathVariable Long id,
            @RequestParam Boolean available) throws Exception {
        return ResponseEntity.ok(mealService.updateAvailability(id, available));
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) throws Exception {
        mealService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
