package com.example.risotto.core.helper;

import com.example.risotto.data.db.entity.FavoriteMealEntity;
import com.example.risotto.data.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMealMapper {

    public static Meal toMeal(FavoriteMealEntity entity) {
        if (entity == null) return null;
        Meal meal = new Meal();
        meal.setId(entity.getId());
        meal.setName(entity.getName());
        meal.setCategory(entity.getCategory());
        meal.setArea(entity.getArea());
        meal.setInstructions(entity.getInstructions());
        meal.setThumbnailUrl(entity.getThumbnailUrl());
        meal.setYoutubeUrl(entity.getYoutubeUrl());
        meal.setTags(entity.getTags());
        meal.setIngredients(entity.getIngredients());
        meal.setFavorite(true); // Always true since it's coming from favorites DB
        return meal;
    }

    public static List<Meal> toMealList(List<FavoriteMealEntity> entities) {
        if (entities == null) return new ArrayList<>();
        List<Meal> meals = new ArrayList<>();
        for (FavoriteMealEntity entity : entities) {
            meals.add(toMeal(entity));
        }
        return meals;
    }

    public static FavoriteMealEntity toEntity(Meal meal, String userId) {
        if (meal == null || userId == null) return null;
        return new FavoriteMealEntity(
                userId,
                meal.getId(),
                meal.getName(),
                meal.getCategory(),
                meal.getArea(),
                meal.getInstructions(),
                meal.getThumbnailUrl(),
                meal.getYoutubeUrl(),
                meal.getTags(),
                meal.getIngredients()
        );
    }
}
