package com.example.risotto.data.datasource.remote.meal;

import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Single;


public interface MealRemoteDataSource {

    Single<Meal> getRandomMeal();

    Single<List<Meal>> getLatestMeals();

    Single<Meal> getMealById(String id);

    Single<List<Meal>> searchMealsByName(String name);
    Single<List<Meal>> searchMealsByFirstLetter(String letter);

    Single<List<Category>> getCategories();

    Single<List<Meal>> filterByCategory(String category);
    Single<List<Meal>> filterByArea(String area);
    Single<List<Meal>> filterByIngredient(String ingredient);

    Single<List<Meal>> listAllAreas();
    Single<List<Meal>> listAllCategories();
    Single<List<Meal>> listAllIngredients();
}