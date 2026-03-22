package com.example.risotto.core.helper;

import com.example.risotto.data.db.entity.CachedMealEntity;
import com.example.risotto.data.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class CachedMealMapper {

    public static Meal toMeal(CachedMealEntity entity) {
        if (entity == null) return null;
        Meal meal = new Meal();
        meal.setId(entity.getId());
        meal.setName(entity.getName());
        meal.setCategory(entity.getCategory());
        meal.setArea(entity.getArea());
        meal.setInstructions(entity.getInstructions());
        meal.setThumbnailUrl(entity.getThumbnailUrl());
        meal.setYoutubeUrl(entity.getYoutubeUrl());
        meal.setIngredients(entity.getIngredients());
        return meal;
    }

    public static CachedMealEntity toEntity(Meal meal) {
        if (meal == null || meal.getId() == null) return null;
        return new CachedMealEntity(
                meal.getId(),
                meal.getName(),
                meal.getCategory(),
                meal.getArea(),
                meal.getInstructions(),
                meal.getThumbnailUrl(),
                meal.getYoutubeUrl(),
                meal.getIngredients()
        );
    }
}
