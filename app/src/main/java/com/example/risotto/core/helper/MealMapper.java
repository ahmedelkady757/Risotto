package com.example.risotto.core.helper;

import com.example.risotto.data.model.Ingredient;
import com.example.risotto.data.model.Meal;
import com.example.risotto.data.network.dto.FilterDto;
import com.example.risotto.data.network.dto.MealDto;

import java.util.ArrayList;
import java.util.List;


public final class MealMapper {

    private MealMapper() { }


    public static Meal fromDto(MealDto dto) {
        Meal meal = new Meal();

        meal.setId(dto.getIdMeal());
        meal.setName(dto.getStrMeal());
        meal.setCategory(dto.getStrCategory());
        meal.setArea(dto.getStrArea());
        meal.setInstructions(dto.getStrInstructions());
        meal.setThumbnailUrl(dto.getStrMealThumb());
        meal.setYoutubeUrl(dto.getStrYoutube());
        meal.setTags(dto.getStrTags());
        meal.setIngredients(mapIngredients(dto));
        meal.setFavorite(false);

        return meal;
    }


    public static List<Meal> fromDtoList(List<MealDto> dtos) {
        List<Meal> meals = new ArrayList<>();
        if (dtos == null) return meals;
        for (MealDto dto : dtos) {
            if (dto != null) meals.add(fromDto(dto));
        }
        return meals;
    }


    public static Meal fromFilterDto(FilterDto dto) {
        Meal meal = new Meal();
        meal.setId(dto.getIdMeal());
        meal.setName(dto.getStrMeal());
        meal.setThumbnailUrl(dto.getStrMealThumb());
        meal.setFavorite(false);
        return meal;
    }


    public static List<Meal> fromFilterDtoList(List<FilterDto> dtos) {
        List<Meal> meals = new ArrayList<>();
        if (dtos == null) return meals;
        for (FilterDto dto : dtos) {
            if (dto != null) meals.add(fromFilterDto(dto));
        }
        return meals;
    }


    private static List<Ingredient> mapIngredients(MealDto dto) {
        List<Ingredient> ingredients = new ArrayList<>();

        String[] names    = dto.getIngredients();
        String[] measures = dto.getMeasures();

        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            if (name == null || name.trim().isEmpty()) continue;

            String measure = (measures[i] != null) ? measures[i].trim() : "";
            ingredients.add(new Ingredient(name.trim(), measure));
        }

        return ingredients;
    }
}