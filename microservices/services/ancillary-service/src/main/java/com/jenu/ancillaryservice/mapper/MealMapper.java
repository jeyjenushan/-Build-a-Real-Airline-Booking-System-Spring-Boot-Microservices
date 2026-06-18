package com.jenu.ancillaryservice.mapper;


import com.jenu.ancillaryservice.model.Meal;
import com.jenu.payload.response.MealResponse;

public class MealMapper {

    public static MealResponse toResponse(Meal meal) {
        if (meal == null) {
            return null;
        }

        return MealResponse.builder()
                .id(meal.getId())
                .code(meal.getCode())

                .name(meal.getName())
                .mealType(meal.getMealType())
                .dietaryRestriction(meal.getDietaryRestriction())
                .ingredients(meal.getIngredients())
                .imageUrl(meal.getImageUrl())
                .available(meal.getAvailable())
                .requiresAdvanceBooking(meal.getRequiresAdvanceBooking())
                .advanceBookingHours(meal.getAdvanceBookingHours())
                .displayOrder(meal.getDisplayOrder())
                .airlineId(meal.getAirlineId())
                .createdAt(meal.getCreatedAt())
                .updatedAt(meal.getUpdatedAt())
                .build();
    }
}
