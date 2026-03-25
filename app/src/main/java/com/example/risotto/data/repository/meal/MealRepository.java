package com.example.risotto.data.repository.meal;

import com.example.risotto.data.model.Category;
import com.example.risotto.data.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;


public interface MealRepository {

    Single<Meal> getRandomMeal();
    Single<List<Category>> getCategories();

    Single<Meal> getMealById(String id);

    Single<List<Meal>> searchMealsByName(String name);
    Single<List<Meal>> filterByCategory(String category);

    Completable cacheMeal(Meal meal);
    Completable cacheCategories(List<Category> categories);
    Single<Meal> getCachedRandomMeal();
    Single<Meal> getCachedMealById(String id);
    Flowable<List<Meal>> getCachedTopMeals();
    Flowable<List<Category>> getCachedCategories();
}